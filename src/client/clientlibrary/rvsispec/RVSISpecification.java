/**
 * 
 */
package client.clientlibrary.rvsispec;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import kvs.table.CompoundKey;

/**
 * @author hengxin
 * @date Created: 10-27-2015
 * 
 * Specification for rvsi, including k1-bv (backward view), k2-fv (forward view), and k3-sv (snapshot view)
 */
public abstract class RVSISpecification
{
	private Map<HashSet<CompoundKey>, Integer> spec_map = new HashMap<>();
	
	public void addSpec(HashSet<CompoundKey> ckeys, int bound)
	{
		this.spec_map.put(ckeys, bound);
	}

	public Map<HashSet<CompoundKey>, Integer> getSpecMap()
	{
		return this.spec_map;
	}
	
	public abstract boolean check();
}
