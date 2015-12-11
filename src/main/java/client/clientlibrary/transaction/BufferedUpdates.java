package client.clientlibrary.transaction;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.base.MoreObjects;

import kvs.component.Cell;
import kvs.component.Column;
import kvs.component.Row;
import kvs.compound.CompoundKey;
import net.jcip.annotations.NotThreadSafe;

/**
 * Update records for write operations in a transaction.
 * 
 * @author hengxin
 * @date Created on 10-27-2015
 * 
 * @implNote
 * 	It uses {@link LinkedHashMap}, which is not thread-safe.
 */
@NotThreadSafe
public class BufferedUpdates implements Serializable
{
	private static final long serialVersionUID = 8322087463777227998L;

	// TODO Do you really need the insertion-order of {@link LinkedHashMap}; or try {@link HashMap}
	private final Map<CompoundKey, Cell> buffered_update_map = new LinkedHashMap<>();
	
	public void intoBuffer(CompoundKey ck, Cell data)
	{
		this.buffered_update_map.put(ck, data);
	}
	
	public void intoBuffer(Row r, Column c, Cell data)
	{
		this.buffered_update_map.put(new CompoundKey(r, c), data);
	}
	
	public Map<CompoundKey, Cell> getBufferedUpdateMap()
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