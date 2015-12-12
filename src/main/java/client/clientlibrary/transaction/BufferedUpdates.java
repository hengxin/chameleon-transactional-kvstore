package client.clientlibrary.transaction;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.base.MoreObjects;

import kvs.component.Cell;
import kvs.component.Column;
import kvs.component.Ordinal;
import kvs.component.Row;
import kvs.component.Timestamp;
import kvs.compound.CKeyToOrdinalIndex;
import kvs.compound.CompoundKey;
import kvs.compound.ITimestampedCell;
import kvs.compound.TimestampedCell;
import net.jcip.annotations.NotThreadSafe;

/**
 * Update records for write operations in a transaction.
 * 
 * @author hengxin
 * @date Created on 10-27-2015
 * 
 * @implNote
 * 	It uses {@link LinkedHashMap}, which is not thread-safe.
 *  At the <i>client</i> side, it is only accessed by the single client thread.
 *  At the <i>master</i> side, it is also only accessed by a single thread, when 
 *  its transaction is to commit.
 */
@NotThreadSafe
public final class BufferedUpdates implements Serializable
{
	private static final long serialVersionUID = 8322087463777227998L;

	// TODO Do you really need the insertion-order of {@link LinkedHashMap}; or try {@link HashMap}
	private final Map<CompoundKey, ITimestampedCell> buffered_update_map = new LinkedHashMap<>();
	
	/**
	 * Buffer the update of (row-col-key, data)
	 * @param ck {@link CompoundKey}
	 * @param data {@link Cell}
	 */
	public void intoBuffer(CompoundKey ck, Cell data)
	{
		this.buffered_update_map.put(ck, new TimestampedCell(data));
	}
	
	/**
	 * Buffer the update of (row, col, data)
	 * @param r {@link Row}
	 * @param c {@link Column}
	 * @param data {@link Cell}
	 */
	public void intoBuffer(Row r, Column c, Cell data)
	{
		this.intoBuffer(new CompoundKey(r, c), data);
	}
	
	/**
	 * Fill the buffered updates ({@link #buffered_update_map}) 
	 * by assigning {@link Timestamp} and {@link Ordinal} to the {@link Cells}. 
	 * @param cts 
	 * 	{@link Timestamp} (commit-timestamp of the transaction of the buffered updates) to assign 
	 * @param ck_ord_index 
	 * 	Index of {@link Ordinal} for each {@link CompoundKey}; used to get the next ordinal. 
	 */
	public BufferedUpdates fillTsAndOrd(Timestamp cts, CKeyToOrdinalIndex ck_ord_index)
	{
		this.buffered_update_map.entrySet()
			.forEach(ck_tscell_entry -> 
			{
				CompoundKey ck = ck_tscell_entry.getKey();
				ITimestampedCell ts_cell = ck_tscell_entry.getValue();
				
				Ordinal current_ord = ck_ord_index.get(ck);
				Ordinal next_ord = current_ord.incrementAndGet();
				
				this.buffered_update_map.put(ck, new TimestampedCell(cts, next_ord, ts_cell.getCell()));
			});
		
		return this;
	}
	
	public Map<CompoundKey, ITimestampedCell> getBufferedUpdateMap()
	{
		return this.buffered_update_map;
	}
	
	public Set<CompoundKey> getUpdatedCKeys()
	{
		return this.buffered_update_map.keySet();
	}
	
	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(this)
				.add("BufferedUpdates", this.buffered_update_map)
				.toString();
	}
}