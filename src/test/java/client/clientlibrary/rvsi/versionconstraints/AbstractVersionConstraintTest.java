package client.clientlibrary.rvsi.versionconstraints;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Triple;
import org.junit.Before;
import org.junit.Test;

import client.clientlibrary.rvsi.rvsispec.AbstractRVSISpecification;
import client.clientlibrary.rvsi.rvsispec.BVSpecification;
import client.clientlibrary.transaction.QueryResults;
import kvs.component.Cell;
import kvs.component.Timestamp;
import kvs.compound.CompoundKey;
import kvs.compound.TimestampedCell;

public class AbstractVersionConstraintTest
{
	private AbstractRVSISpecification rvsi_spec = new BVSpecification();
	private QueryResults query_results = new QueryResults();

	private CompoundKey ck_r1_c1 = new CompoundKey("R1", "C1");
	private CompoundKey ck_r1_c2 = new CompoundKey("R1", "C2");
	private CompoundKey ck_r2_c1 = new CompoundKey("R2", "C1");
	private CompoundKey ck_r2_c2 = new CompoundKey("R2", "C2");
	
	private TimestampedCell tc1 = new TimestampedCell(Timestamp.TIMESTAMP_INIT, new Cell("R1C1"));
	private TimestampedCell tc2 = new TimestampedCell(Timestamp.TIMESTAMP_INIT, new Cell("R2C2"));
	
	@Before
	public void setUp() throws Exception
	{
		// set the {@value #rvsi_spec}
		HashSet<CompoundKey> ckey_set_r1 = Stream.of(ck_r1_c1, ck_r1_c2)
				.collect(Collectors.toCollection(HashSet::new));
		
		HashSet<CompoundKey> ckey_set_r2 = Stream.of(ck_r2_c1, ck_r2_c2)
				.collect(Collectors.toCollection(HashSet::new));
		
		this.rvsi_spec.addSpec(ckey_set_r1, 1);
		this.rvsi_spec.addSpec(ckey_set_r2, 2);
		
		// set the the {@link #query_results}
		this.query_results.put(ck_r1_c1, tc1);
		this.query_results.put(ck_r2_c2, tc2);
	}
	
	@Test
	public void testCheck()
	{
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testExtractVersionConstraintElements()
	{
		List<Triple<CompoundKey, TimestampedCell, Integer>> triples = AbstractVersionConstraint.extractVersionConstraintElements(this.rvsi_spec, this.query_results);
		
		@SuppressWarnings("serial")
		List<Triple<CompoundKey, TimestampedCell, Integer>> expected_triples = new ArrayList<Triple<CompoundKey, TimestampedCell, Integer>>()
			{
				{
					add(Triple.of(ck_r1_c1, tc1, 1));
					add(Triple.of(ck_r2_c2, tc2, 2));
				}
			};
			
		assertThat(triples, containsInAnyOrder(expected_triples.toArray()));
	}

}
