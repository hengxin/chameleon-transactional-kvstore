/**
 * @copyright See {@link utils.intervaltree#package-info.java}.
 */
package utils.intervaltree;

import org.jetbrains.annotations.NotNull;

/**
 * @param <K>
 */
public class Interval<K extends Comparable<? super K>> {
	
	private K low, high;
	
	public Interval(K low, K high){
		this.setLow(low);
		this.setHigh(high);
	}

	K getHigh() {
		return high;
	}

	void setHigh(K high) {
		this.high = high;
	}

	K getLow() {
		return low;
	}

	void setLow(K low) {
		this.low = low;
	}
	
	boolean contains(@NotNull K p){
		return low.compareTo(p) <= 0 && high.compareTo(p) > 0;
	}
	
	/**
	 * Returns true if this Interval wholly contains i.
	 */
	boolean contains(@NotNull Interval<K> i){
		return contains(i.low) && contains(i.high);
	}
	
	boolean overlaps(@NotNull K low, @NotNull K high){
		boolean res = this.low.compareTo(high) <= 0 && this.high.compareTo(low) > 0;
		return res;
	}
	
	boolean overlaps(@NotNull Interval<K> i){
		return overlaps(i.low,i.high);
	}
	
	@NotNull
    @Override
	public String toString() {
		return "Interval["+low+"-"+high+"]";
	}

}
