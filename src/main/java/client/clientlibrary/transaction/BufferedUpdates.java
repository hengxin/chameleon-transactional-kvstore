package client.clientlibrary.transaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.base.MoreObjects;

import kvs.component.Cell;
import kvs.component.Column;
import kvs.component.Ordinal;
import kvs.component.Row;
import kvs.component.Timestamp;
import kvs.compound.CKeyToOrdinalIndex;
import kvs.compound.CompoundKey;
import kvs.compound.ITimestampedCell;
import kvs.compound.KVItem;
import kvs.compound.TimestampedCell;
import net.jcip.annotations.NotThreadSafe;

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
 *  At the <i>master</i> side, it is also only accessed by a single thread, when 
 *  its transaction is to commit.
 */
@NotThreadSafe
public final class BufferedUpdates implements Serializable {

	private static final long serialVersionUID = 8322087463777227998L;

	private final List<KVItem> item_list;
	
	public BufferedUpdates() {
		this.item_list = new ArrayList<>();
	}

	public BufferedUpdates(final List<KVItem> kv_item_list) {
		this.item_list = kv_item_list;
	}
	
	/**
	 * Buffer the update of (row&col-key, cell)
	 * @param ck {@link CompoundKey} to update
	 * @param cell {@link Cell} value
	 */
	public void intoBuffer(CompoundKey ck, Cell cell) {
		this.intoBuffer(new KVItem(ck, cell));
	}
	
	/**
	 * Buffer the update of (row, col, cell)
	 * @param r {@link Row}
	 * @param c {@link Column}
	 * @param cell {@link Cell}
	 */
	public void intoBuffer(Row r, Column c, Cell cell) {
		this.intoBuffer(new KVItem(r, c, cell));
	}
	
	/**
	 * Buffer the update represented by a {@link KVItem}.
	 * @param kv_item	{@link KVItem} representing an update
	 */
	public void intoBuffer(KVItem kv_item) {
		this.item_list.add(kv_item);
	}
	
	/**
	 * Return a new {@link BufferedUpdates} which fills {@link #item_list} 
	 * by assigning {@link Timestamp} and {@link Ordinal} to the {@link Cells}. 
	 * 
	 * @param cts 
	 * 	{@link Timestamp} (commit-timestamp of the transaction of the buffered updates) to assign 
	 * @param ck_ord_index 
	 * 	Index of {@link Ordinal} for each {@link CompoundKey}; used to get the next ordinal. 
	 * 
	 * FIXME check the lambda expression
	 */
	public BufferedUpdates fillTsAndOrd(Timestamp cts, CKeyToOrdinalIndex ck_ord_index) {
		return new BufferedUpdates( 
			this.item_list.stream()
				.map(kv_item -> {
						CompoundKey ck = kv_item.getCK();
						ITimestampedCell ts_cell = kv_item.getTsCell();
						
						Ordinal current_ord = ck_ord_index.get(ck);
						Ordinal next_ord = current_ord.incrementAndGet();
						
						return new KVItem(ck, new TimestampedCell(cts, next_ord, ts_cell.getCell()));
					})
				.collect(Collectors.toList()));
	}
	
	public Stream<KVItem> stream() {
		return this.item_list.stream();
	}
	
	public Set<CompoundKey> getUpdatedCKeys() {
		return this.item_list.stream()
				.map(KVItem::getCK)
				.collect(Collectors.toSet());
	}
	
	/**
	 * Merges two {@link BufferedUpdates} and returns a new one. It does not modify the original ones. 
	 * @param first_updates		{@link BufferedUpdates} to merge
	 * @param second_updates	{@link BufferedUpdates} to merge
	 * @return	a new {@link BufferedUpdates} which merges the two original ones
	 */
	public static BufferedUpdates merge(BufferedUpdates first_updates, BufferedUpdates second_updates) {
		List<KVItem> item_list = new ArrayList<>();

		item_list.addAll(first_updates.item_list);
		item_list.addAll(second_updates.item_list);

		return new BufferedUpdates(item_list);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.item_list);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(o == null)
			return false;
		if(o.getClass() != this.getClass())
			return false;
		
		BufferedUpdates that = (BufferedUpdates) o;
		
		return Objects.equals(this.item_list, that.item_list);
	}
	
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("BufferedUpdates", this.item_list)
				.toString();
	}
}