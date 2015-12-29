package twopc.partitioning;

import java.util.List;
import java.util.Map;

import client.clientlibrary.transaction.BufferedUpdates;
import kvs.component.Column;
import kvs.component.Row;
import kvs.compound.KVItem;
import site.ISite;

/**
 * A simple static partitioner which partitions 
 * data according to their keys (in alphabetical order). 
 * @author hengxin
 * @date Created on Dec 27, 2015
 */
public enum AlphabeticalStaticPartitioner implements IPartitioner
{
	INSTANCE;
	
	@Override
	public ISite locateSiteFor(Row row, Column col)
	{
		return null;
	}

	@Override
	public Map<ISite, List<KVItem>> locateSitesFor(BufferedUpdates updates)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
