package client.clientlibrary.transaction;

import com.google.common.base.MoreObjects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.concurrent.NotThreadSafe;

import kvs.component.Cell;
import kvs.component.Column;
import kvs.component.Ordinal;
import kvs.component.Row;
import kvs.component.Timestamp;
import kvs.compound.CKeyToOrdinal;
import kvs.compound.CompoundKey;
import kvs.compound.ITimestampedCell;
import kvs.compound.KVItem;
import kvs.compound.TimestampedCell;

/**
 * Update records for write operations in a transaction.
 * <b>WARN:</b> don't support "repeated write (on the same key)"
 * in the same transaction.
 * 
 * @author hengxin
 * @date Created on 10-27-2015
 * 
 * @implNote
 * 	It uses {@link ArrayList}, which is not thread-safe.
 *  At the <i>client</i> side, it is only accessed by the single client thread.
 *  At the <i>master</i> side, it is also only accessed by a single thread,
 *  when its transaction is to commit.
 */
@NotThreadSafe
public final class BufferedUpdates implements Serializable {
	private static final long serialVersionUID = 8322087463777227998L;
    private static final Logger LOGGER = LoggerFactory.getLogger(BufferedUpdates.class);

    // TODO: refactor to Map<CompoundKey, Cell> ???
	private final List<KVItem> itemList;
	
	public BufferedUpdates() { itemList = new ArrayList<>(); }
	public BufferedUpdates(final List<KVItem> kvItemList) { itemList = kvItemList; }
	
	/**
	 * Buffer the update of (row&col-key, cell)
	 * @param ck {@link CompoundKey} to update
	 * @param cell {@link Cell} value
	 */
	public void intoBuffer(CompoundKey ck, Cell cell) { intoBuffer(new KVItem(ck, cell)); }
	
	/**
	 * Buffer the update of (row, col, cell)
	 * @param r {@link Row}
	 * @param c {@link Column}
	 * @param cell {@link Cell}
	 */
	public void intoBuffer(Row r, Column c, Cell cell) { intoBuffer(new KVItem(r, c, cell)); }
	
	/**
	 * Buffer the update represented by a {@link KVItem}.
	 * @param kvItem	{@link KVItem} representing an update
	 */
	public void intoBuffer(KVItem kvItem) { itemList.add(kvItem); }

    /**
     * Look up the last update on {@code r} + {@code c} in the same transaction.
     * @param r {@link Row} to look up
     * @param c {@link Column} to look up
     * @return  the last update {@link ITimestampedCell} on {@code r} + {@code c};
     *  it may be {@code null} if no such updates at all.
     */
	@Nullable
    public ITimestampedCell lookup(Row r, Column c) {
	    return lookup(new CompoundKey(r, c));
    }

    /**
     * Look up the last update on {@code ck} in the same transaction.
     * @param ck  {@link CompoundKey} to look up
     * @return  the last update {@link ITimestampedCell} on {@code ck};
     *  it may be {@code null} if no such updates at all.
     */
	public ITimestampedCell lookup(@NotNull CompoundKey ck) {
        KVItem kvItem;

	    // reverse iteration
        ListIterator<KVItem> iter = itemList.listIterator(itemList.size());
        while (iter.hasPrevious()) {
            kvItem = iter.previous();
            if (ck.equals(kvItem.getCK()))
                return kvItem.getTsCell();
        }

        return null;
    }

	/**
	 * Return a new {@link BufferedUpdates} which fills {@link #itemList}
	 * by assigning {@link Timestamp} and {@link Ordinal} to the {@link Cell}s.
	 * 
	 * @param cts 
	 * 	{@link Timestamp} (commit-timestamp of the transaction of the buffered updates) to assign 
	 * @param ckOrdIndex
	 * 	Index of {@link Ordinal} for each {@link CompoundKey}; used to lookup the next ordinal.
	 *
     * @implNote
     *  (1) This method will modify the parameter {@code ckOrdIndex};
     *  (2) This method will modify the underlying {@link #itemList} of this {@link BufferedUpdates}.
	 */
	@NotNull
    public BufferedUpdates fillTsAndOrd(@NotNull final Timestamp cts, @NotNull CKeyToOrdinal ckOrdIndex) {
	    itemList.replaceAll(kvItem -> {
            CompoundKey ck = kvItem.getCK();
            ITimestampedCell tsCell = kvItem.getTsCell();

            Ordinal currentOrd = ckOrdIndex.lookup(ck);
            Ordinal nextOrd = currentOrd.incrementAndGet();

            LOGGER.debug("In BufferedUpdates, cts is [{}], ck is [{}], and ord is [{}]",
                    cts, ck, nextOrd);

            return new KVItem(ck, new TimestampedCell(cts, nextOrd, tsCell.getCell()));
        });

        return this;
	}
	
	public Stream<KVItem> stream() { return itemList.stream(); }
	
	public Set<CompoundKey> getUpdatedCKeys() {
		return itemList.stream()
				.map(KVItem::getCK)
				.collect(Collectors.toSet());
	}
	
	/**
	 * Merges two {@link BufferedUpdates} and returns a new one.
     * It does not modify the original ones.
     *
	 * @param firstUpdates		{@link BufferedUpdates} to merge
	 * @param secondUpdates	{@link BufferedUpdates} to merge
	 * @return	a new {@link BufferedUpdates} which merges the two original ones
	 */
	@NotNull
    public static BufferedUpdates merge(@NotNull BufferedUpdates firstUpdates, @NotNull BufferedUpdates secondUpdates) {
		List<KVItem> items = new ArrayList<>();

		items.addAll(firstUpdates.itemList);
		items.addAll(secondUpdates.itemList);

		return new BufferedUpdates(items);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(itemList);
	}
	
	@Override
	public boolean equals(@Nullable Object o) {
		if(o == this)
			return true;
		if(o == null)
			return false;
		if(o.getClass() != this.getClass())
			return false;
		
		BufferedUpdates that = (BufferedUpdates) o;
		
		return Objects.equals(this.itemList, that.itemList);
	}
	
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("BufferedUpdates", this.itemList)
				.toString();
	}

}