package client.clientlibrary.transaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
public final class BufferedUpdates implements Serializable
{
	private static final long serialVersionUID = 8322087463777227998L;

	private final List<KVItem> buffered_update_list;
	
	public BufferedUpdates()
	{
		this.buffered_update_list = new ArrayList<>();
	}

	private BufferedUpdates(List<KVItem> kv_item_list)
	{
		this.buffered_update_list = kv_item_list;
	}
	
	/**
	 * Buffer the update of (row&col-key, cell)
	 * @param ck {@link CompoundKey}
	 * @param cell {@link Cell}
	 */
	public void intoBuffer(CompoundKey ck, Cell cell)
	{
		this.buffered_update_list.add(new KVItem(ck, cell));
	}
	
	/**
	 * Buffer the update of (row, col, cell)
	 * @param r {@link Row}
	 * @param c {@link Column}
	 * @param cell {@link Cell}
	 */
	public void intoBuffer(Row r, Column c, Cell cell)
	{
		this.buffered_update_list.add(new KVItem(r, c, cell));
	}
	
	/**
	 * Return a new {@link BufferedUpdates} which fills {@link #buffered_update_list} 
	 * by assigning {@link Timestamp} and {@link Ordinal} to the {@link Cells}. 
	 * 
	 * @param cts 
	 * 	{@link Timestamp} (commit-timestamp of the transaction of the buffered updates) to assign 
	 * @param ck_ord_index 
	 * 	Index of {@link Ordinal} for each {@link CompoundKey}; used to get the next ordinal. 
	 */
	public BufferedUpdates fillTsAndOrd(Timestamp cts, CKeyToOrdinalIndex ck_ord_index)
	{
		return new BufferedUpdates( 
			this.buffered_update_list.parallelStream()
				.map(kv_item -> 
					{
						CompoundKey ck = kv_item.getCK();
						ITimestampedCell ts_cell = kv_item.getTsCell();
						
						Ordinal current_ord = ck_ord_index.get(ck);
						Ordinal next_ord = current_ord.incrementAndGet();
						
						return new KVItem(ck, new TimestampedCell(cts, next_ord, ts_cell.getCell()));
					})
				.collect(Collectors.toList()));
	}
	
	public Stream<KVItem> parallelStream()
	{
		return this.buffered_update_list.parallelStream();
	}
	
	public Set<CompoundKey> getUpdatedCKeys()
	{
		return this.buffered_update_list.parallelStream()
				.map(kv_item -> kv_item.getCK())
				.collect(Collectors.toSet());
	}
	
	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(this)
				.add("BufferedUpdates", this.buffered_update_list)
				.toString();
	}
}