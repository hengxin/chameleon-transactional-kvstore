package twopc.partitioning;

import kvs.component.Column;
import kvs.component.Row;
import site.ISite;

/**
 * A simple static partitioner which partitions 
 * data according to their keys (in alphabetical order). 
 * @author hengxin
 * @date Created on Dec 27, 2015
 */
public final class AlphabeticalStaticPartitioner implements IPartitioner
{
	@Override
	public ISite getSiteFor(Row row, Column col)
	{
		return null;
	}
}
