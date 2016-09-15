/**
 * 
 */
package kvs.component;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

import kvs.table.AbstractTable;

/**
 * @author hengxin
 * @date Created: 10-27-2015
 * 
 * Row keys of the {@link AbstractTable}.
 */
public class Row implements Comparable<Row>, Serializable {
	private static final long serialVersionUID = -971488511398300319L;

	private final String row;
	
	public Row(String key) { this.row = key; }
	public String getRow() { return this.row; }

	@Override
	public int compareTo(@NotNull Row that) { return ComparisonChain.start().compare(this.row, that.row).result(); }
	
	@Override
	public int hashCode() { return Objects.hashCode(this.row); }
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(o == null)
			return false;
		if(! (o instanceof Row))
			return false;
		
		Row that = (Row) o;
		return Objects.equal(this.row, that.row);
	}
	
    @Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
                .add("row", row)
                .toString();
	}
}
