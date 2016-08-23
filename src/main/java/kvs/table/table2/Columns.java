package kvs.table.table2;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import kvs.component.Column;
import kvs.component.Row;
import kvs.table.ITimestampedCellStore;

/**
 * @author hengxin
 * @date Created on Dec 19, 2015
 */
public class Columns {
	private final ConcurrentMap<Column, ITimestampedCellStore> columns;
	
	public Columns(Row row) { columns = new ConcurrentHashMap<>(); }
	
	public ITimestampedCellStore get(Column col) {
//		return this.columns.computeIfAbsent(col -> )
		return null;
	}
}
