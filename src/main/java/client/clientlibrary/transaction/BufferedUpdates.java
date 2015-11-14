package client.clientlibrary.transaction;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.common.base.MoreObjects;

import kvs.component.Cell;
import kvs.component.Column;
import kvs.component.Row;
import kvs.compound.CompoundKey;

/**
 * @author hengxin
 * @date 10-27-2015
 * 
 * <p> Update records for write operations in a transaction.
 * 
 * <b>Note:</b> It uses {@link LinkedHashMap}, which is not thread-safe.
 */
public class BufferedUpdates implements Serializable
{
	private static final long serialVersionUID = 8322087463777227998L;

	// TODO Do you really need the insertion-order of {@link LinkedHashMap}; or try {@link HashMap}
	private Map<CompoundKey, Cell> buffered_update_map = new LinkedHashMap<>();
	
	public void intoBuffer(CompoundKey ck, Cell data)
	{
		this.buffered_update_map.put(ck, data);
	}
	
	public void intoBuffer(Row r, Column c, Cell data)
	{
		this.buffered_update_map.put(new CompoundKey(r, c), data);
	}
	
	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(this)
				.add("BufferedUpdates", this.buffered_update_map)
				.toString();
	}
}