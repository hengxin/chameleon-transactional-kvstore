package twopc.partitioning;

import java.util.List;
import java.util.Map;

import client.clientlibrary.transaction.BufferedUpdates;
import kvs.component.Column;
import kvs.component.Row;
import kvs.compound.KVItem;

/**
 * A simple static partitioner which partitions 
 * data according to their keys (in alphabetical order). 
 * @author hengxin
 * @date Created on Dec 27, 2015
 */
public final class AlphabeticalStaticPartitioner implements IPartitioner
{
	private final int buckets;
	
	public AlphabeticalStaticPartitioner(int buckets)
	{
		this.buckets = buckets;
	}

	@Override
	public int locateSiteFor(Row row, Column col, int buckets)
	{
		return -1;
	}

	@Override
	public Map<Integer, List<KVItem>> locateSitesFor(BufferedUpdates updates, int buckets)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
