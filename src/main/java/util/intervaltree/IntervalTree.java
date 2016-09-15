/**
 * @copyright See {@link util.intervaltree#package-info.java}.
 */
package util.intervaltree;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class IntervalTree<K extends Comparable<? super K>, V> {
	
	@Nullable
    private IntervalTreeNode<K, V> root = null;

	@NotNull
    public Collection<V> searchOverlapping(Interval<K> range){
		Collection<V> c = new LinkedList<V>();
		if(root != null){
			root.searchOverlapping(range, c);
		}
		return c;
	}
	
	@NotNull
    public Collection<V> searchOverlapping(K low, K high){
		return searchOverlapping(new Interval<K>(low, high));
	}
	
	/**
	 * Returns a collection of values that wholly contain the range specified.
	 */
	@NotNull
    public Collection<V> searchContaining(Interval<K> range){
		Collection<V> c = new LinkedList<V>();
		if(root != null){
			root.searchContaining(range, c);
		}
		return c;
	}
	
	/**
	 * Returns a collection of values that wholly contain the range specified.
	 */
	@NotNull
    public Collection<V> searchContaining(K low, K high){
		return searchContaining(new Interval<K>(low, high));
	}
	
	/**
	 * Returns a collection of values that are wholly contained by the range specified.
	 */
	@NotNull
    public Collection<V> searchContainedBy(Interval<K> range){
		Collection<V> c = new LinkedList<V>();
		if(root != null){
			root.searchContainedBy(range, c);
		}
		return c;
	}
	
	/**
	 * Returns a collection of values that are wholly contained by the range specified.
	 */
	@NotNull
    public Collection<V> searchContainedBy(K low, K high){
		return searchContainedBy(new Interval<K>(low, high));
	}
	
	public void removeOverlapping(Interval<K> range){
		if(root != null){
			root = root.removeOverlapping(range);
		}
	}
	
	public void removeOverlapping(K low, K high){
		removeOverlapping(new Interval<K>(low, high));
	}
	
	/**
	 * Returns a collection of values that wholly contain the range specified.
	 */
	public void removeContaining(Interval<K> range){
		if(root != null){
			root = root.removeContaining(range);
		}
	}
	
	/**
	 * Returns a collection of values that wholly contain the range specified.
	 */
	public void removeContaining(K low, K high){
		removeContaining(new Interval<K>(low, high));
	}
	
	/**
	 * Returns a collection of values that are wholly contained by the range specified.
	 */
	public void removeContainedBy(Interval<K> range){
		if(root != null){
			root = root.removeContainedBy(range);
		}
	}
	
	/**
	 * Returns a collection of values that are wholly contained by the range specified.
	 */
	public void removeContainedBy(K low, K high){
		removeContainedBy(new Interval<K>(low, high));
	}
	
	public boolean isEmpty() {
		return root == null;
	}

	public void put(Interval<K> key, V value) {
		if(root == null){
			root = new IntervalTreeLeaf<K, V>(key, value);
		}else{
			root = root.put(key,value);
		}
	}
	
	public void put(K low, K high, V value) {
		put(new Interval<K>(low, high),value);
	}
	
	
	public int size() {
		return values().size();
	}

	@NotNull
    public Collection<V> values() {
		Collection<V> c = new LinkedList<V>();
		if(root != null){
			root.values(c);
		}
		return c;
	}

	@NotNull
    public Set<Interval<K>> keySet() {
		Set<Interval<K>> s = new HashSet<Interval<K>>();
		if(root != null){
			root.keySet(s);
		}
		return s;
	}

	public void putAll(@NotNull Map<Interval<K>, V> m) {
		for(Interval<K> i : m.keySet()){
			put(i, m.get(i));
		}
	}

	public void clear() {
		root = null;
	}

	public boolean containsValue(V value) {
		return root != null && root.containsValue(value);
	}

	@NotNull
    public Set<Entry<Interval<K>, V>> entrySet() {
		Set<Entry<Interval<K>, V>> s = new HashSet<Entry<Interval<K>, V>>();
		if(root != null){
			root.entrySet(s);
		}
		return s;
	}

	public void remove(V value) {
		if(root != null){
			root = root.remove(value);
		}
	}
	
	public void removeAll(Collection<V> values) {
		if(root != null){
			root = root.removeAll(values);
		}
	}

	public int height() {
		return root != null ? root.maxHeight() : 0;
	}

	public double averageHeight() {
		if(root == null){
			return 0.0;
		}
		int total = 0;
		int count = 0;
		Collection<Integer> c = new LinkedList<Integer>();
		root.averageHeight(c, 0);
		for(int i : c){
			total += i;
			count ++;
		}
		return (double) total / count;
		
	}

}
