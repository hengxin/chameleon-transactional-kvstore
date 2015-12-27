package twopc.partitioning;

import kvs.component.Column;
import kvs.component.Row;
import site.ISite;

/**
 * A partitioner is responsible for partitioning a large
 * data set into multiple smaller data <i>shards</i>.
 * @author hengxin
 * @date Created on Dec 27, 2015
 */
public interface IPartitioner
{
	/**
	 * Given a key ({@link Row} key + {@link Column} key), the partitioner
	 * returns the {@link ISite} which is responsible for it.
	 * @param row {@link Row} key
	 * @param col {@link Column} key
	 * @return the {@link ISite} responsible for the given key
	 */
	public abstract ISite getSiteFor(Row row, Column col);
}
