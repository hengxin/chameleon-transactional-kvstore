package kvs.table.table2;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

import kvs.component.Column;
import kvs.component.Row;
import kvs.compound.ITimestampedCell;
import kvs.table.AbstractTable;

/**
 * An alternative implementation for {@link AbstractTable},
 * using two-dimensional {@link ConcurrentSkipListMap}.
 * 
 * @author hengxin
 * @date Created on Dec 19, 2015
 */
public class AbstractTable2 {
	private final ConcurrentMap<Row, Columns> table = new ConcurrentHashMap<>();
	
	public ITimestampedCell get(Row row, Column col) {
//		this.table.computeIfAbsent(row, Columns::new)
		return null;
	}
}
