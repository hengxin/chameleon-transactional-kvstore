/**
 * @copyright See {@link util.intervaltree#package-info.java}.
 */
package util.intervaltree;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;

public class IntervalTreeBranch<K extends Comparable<? super K>, V> implements IntervalTreeNode<K, V> {
	
	private IntervalTreeNode<K, V> left,right;
	private Interval<K> key;
	
	public IntervalTreeBranch(IntervalTreeNode<K, V> left,IntervalTreeNode<K, V> right){
		this.left  = left;
		this.right = right;
		updateKeyRange();
	}

	private void updateKeyRange() {
		if(left != null){
			if (right == null || left.getHigh().compareTo(right.getHigh()) > 0) {
				key = new Interval<K>(left.getLow(),left.getHigh());
			}else{
				key = new Interval<K>(left.getLow(),right.getHigh());
			}
		}else{
			key = new Interval<K>(right.getLow(), right.getHigh());
		}
	}

	@Override
	public boolean isLeaf() {
		return false;
	}

	@Override
	public IntervalTreeNode<K, V> getLeft() {
		return left;
	}

	@Override
	public IntervalTreeNode<K, V> getRight() {
		return right;
	}

	void setLeft(IntervalTreeNode<K, V> left) {
		this.left = left;
	}

	void setRight(IntervalTreeNode<K, V> right) {
		this.right = right;
	}

	@Override
	public boolean contains(K point) {
		return key.contains(point);
	}
	
	@Override
	public boolean contains(Interval<K> interval) {
		return key.contains(interval);
	}

	@Override
	public boolean overlaps(K low, K high) {
		return key.overlaps(low, high);
	}

	@Override
	public boolean overlaps(Interval<K> interval) {
		return key.overlaps(interval);
	}

	@Override
	public K getLow() {
		return key.getLow();
	}

	@Override
	public K getHigh() {
		return key.getHigh();
	}

	@Override
	public V getValue() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IntervalTreeNode<K, V> put(Interval<K> key, V value) {
		if(right == null){
			if(left.getLow().compareTo(key.getLow()) < 0){
				right = left;
				left = new IntervalTreeLeaf<K, V>(key, value);
			}else{
				right = new IntervalTreeLeaf<K, V>(key, value);
			}
		}else{
			if(right.getLow().compareTo(key.getLow()) < 0){
				right = right.put(key,value); 
			}else{
				if(left == null){
					left = new IntervalTreeLeaf<K, V>(key, value);
				}else{
					left = left.put(key,value);
				}
			}
		}
		updateKeyRange();
		return this;
	}

	@Override
	public void searchOverlapping(Interval<K> range, Collection<V> accumulator) {
		if(left != null && left.overlaps(range)){
			left.searchOverlapping(range, accumulator);
		}
		if(right != null && right.overlaps(range)){
			right.searchOverlapping(range, accumulator);
		}
	}

	@Override
	public void searchContaining(Interval<K> range, Collection<V> accumulator) {
		if(left != null && left.contains(range)){
			left.searchContaining(range, accumulator);
		}
		if(right != null && right.contains(range)){
			right.searchContaining(range, accumulator);
		}
	}

	@Override
	public int size() {
		return 1 + (left != null ? left.size() : 0) + (right != null ? right.size() : 0);
	}

	@Override
	public void values(Collection<V> accumulator) {
		if(left != null){
			left.values(accumulator);
		}
		if(right != null){
			right.values(accumulator);
		}
	}

	@Override
	public IntervalTreeNode<K, V> remove(V value) {
		if(left != null){
			left = left.remove(value);
		}
		if(right != null){
			right = right.remove(value);
		}
		return removeCleanup();
	}

	private IntervalTreeNode<K, V> removeCleanup() {
		if(left == null){
			if(right == null){
				return null;
			}else{
				return right;
			}
		}else if(right == null){
			return left;
		}
		updateKeyRange();
		return this;
	}

	@Override
	public void entrySet(Set<Entry<Interval<K>, V>> accumulator) {
		if(left != null){
			left.entrySet(accumulator);
		}
		if(right != null){
			right.entrySet(accumulator);
		}
	}

	@Override
	public boolean containsValue(V value) {
		return (left != null && left.containsValue(value)) || (right != null && right.containsValue(value)); 
	}

	@Override
	public void keySet(Set<Interval<K>> accumulator) {
		if(left != null){
			left.keySet(accumulator);
		}
		if(right != null){
			right.keySet(accumulator);
		}
	}

	@Override
	public boolean containedBy(Interval<K> interval) {
		return interval.contains(key);
	}

	@Override
	public void searchContainedBy(Interval<K> range, Collection<V> accumulator) {
		if(left != null && left.overlaps(range)){
			left.searchContainedBy(range, accumulator);
		}
		if(right != null && right.overlaps(range)){
			right.searchContainedBy(range, accumulator);
		}
	}

	@Override
	public Interval<K> getRange() {
		return key;
	}

	@Override
	public int maxHeight() {
		if(left != null){
			if(right != null){
				return Math.max(left.maxHeight(), right.maxHeight()) + 1;
			}else{
				return left.maxHeight() + 1;
			}
		}else{
			return right.maxHeight() + 1;
		}
	}
	
	@Override
	public void averageHeight(Collection<Integer> heights, int currentHeight) {
		if(left != null){
			left.averageHeight(heights, currentHeight + 1);
		}
		if(right != null){
			right.averageHeight(heights, currentHeight + 1);
		}
	}

	@Override
	public IntervalTreeNode<K, V> removeAll(Collection<V> values) {
		if(left != null){
			left = left.removeAll(values);
		}
		if(right != null){
			right = left.removeAll(values);
		}
		if(left == null && right == null){
			return null;
		}
		updateKeyRange();
		return this;
	}

	@Override
	public IntervalTreeNode<K, V> removeOverlapping(Interval<K> range) {
		if(left != null && left.overlaps(range)){
			left = left.removeOverlapping(range);
		}
		if(right != null && right.overlaps(range)){
			right = right.removeOverlapping(range);
		}
		return removeCleanup();
	}

	@Override
	public IntervalTreeNode<K, V> removeContaining(Interval<K> range) {
		if(left != null && left.contains(range)){
			left = left.removeContaining(range);
		}
		if(right != null && right.contains(range)){
			right = right.removeContaining(range);
		}
		return removeCleanup();
	}

	@Override
	public IntervalTreeNode<K, V> removeContainedBy(Interval<K> range) {
		if(left != null && left.overlaps(range)){
			left = left.removeContainedBy(range);
		}
		if(right != null && right.overlaps(range)){
			right = right.removeContainedBy(range);
		}
		return removeCleanup();
	}

}
