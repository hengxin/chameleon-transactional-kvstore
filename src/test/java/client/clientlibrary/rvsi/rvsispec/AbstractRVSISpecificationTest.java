/**
 * 
 */
package client.clientlibrary.rvsi.rvsispec;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kvs.compound.CompoundKey;

/**
 * @author hengxin
 * @date Created on 11-17-2015
 * 
 * <p> Test case for {@link AbstractRVSISpecificationTest}.
 */
public class AbstractRVSISpecificationTest
{
	private AbstractRVSISpecification rvsi_spec = new BVSpecification();

	private CompoundKey ck_r1_c1 = new CompoundKey("R1", "C1");
	private CompoundKey ck_r1_c2 = new CompoundKey("R1", "C2");
	private CompoundKey ck_r2_c1 = new CompoundKey("R2", "C1");
	private CompoundKey ck_r2_c2 = new CompoundKey("R2", "C2");
	
	@Before
	public void setUp() throws Exception
	{
		HashSet<CompoundKey> ckey_set_r1 = Stream.of(ck_r1_c1, ck_r1_c2)
				.collect(Collectors.toCollection(HashSet::new));
		
		HashSet<CompoundKey> ckey_set_r2 = Stream.of(ck_r2_c1, ck_r2_c2)
				.collect(Collectors.toCollection(HashSet::new));
		
		this.rvsi_spec.addSpec(ckey_set_r1, 1);
		this.rvsi_spec.addSpec(ckey_set_r2, 2);
	}

	/**
	 * Test method for {@link client.clientlibrary.rvsi.rvsispec.AbstractRVSISpecification#addSpec(java.util.HashSet, int)}.
	 */
	@Test
	public void testAddSpec()
	{
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link client.clientlibrary.rvsi.rvsispec.AbstractRVSISpecification#flattenRVSISpecMap()}.
	 */
	@Test
	public void testFlattenRVSISpecMap()
	{
		Map<CompoundKey, Integer> flatten_map = this.rvsi_spec.flattenRVSISpecMap();
		
		@SuppressWarnings("serial")
		Map<CompoundKey, Integer> expected_map = new HashMap<CompoundKey, Integer>()
		{
				{
					put(ck_r1_c1, 1);
					put(ck_r1_c2, 1);
					put(ck_r2_c1, 2);
					put(ck_r2_c2, 2);
				}
		};
		
		Assert.assertEquals("Test the flatten method.", expected_map, flatten_map);
	}

	/**
	 * Test method for {@link client.clientlibrary.rvsi.rvsispec.AbstractRVSISpecification#generateVersionConstraint(client.clientlibrary.transaction.QueryResults)}.
	 */
	@Test
	public void testGenerateVersionConstraint()
	{
		fail("Not yet implemented"); // TODO
	}

}
