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
 * Column keys of the {@link AbstractTable}.
 */
public class Column implements Comparable<Column>, Serializable {
	private static final long serialVersionUID = -1528275933592207808L;

	private final String col;
	
	public Column(String key) { this.col = key; }
	public String getCol() { return col; }

	@Override
	public int compareTo(@NotNull Column that)
	{
		return ComparisonChain.start().compare(this.col, that.col).result();
	}

	@Override
	public int hashCode() 
	{
		return Objects.hashCode(this.col);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(o == null)
			return false;
		if(! (o instanceof Column))
			return false;
		
		Column that = (Column) o;
		return Objects.equal(this.col, that.col);
	}
	
    @Override
	public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("col", col)
                .toString();
	}

}
