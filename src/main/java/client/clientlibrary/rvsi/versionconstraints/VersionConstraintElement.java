package client.clientlibrary.rvsi.versionconstraints;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import kvs.compound.CompoundKey;
import kvs.compound.ITimestampedCell;

/**
 * It consists of the basic elements of an {@link AbstractVersionConstraint}:
 * Each version constraint of <em>any</em> of the three types consists of three parts:
 * <p><ul>
 * <li> the {@link CompoundKey} involved
 * <li> the {@link ITimestampedCell} under constraint, 
 * 	corresponding to the {@link CompoundKey} given above 
 * <li> the staleness bound allowed for the {@link ITimestampedCell} given above
 * </ul><p>
 * 
 * @author hengxin
 * @date Created on 11-16-2015
 */
public class VersionConstraintElement
{
	private final CompoundKey vce_ck;
	private final ITimestampedCell vce_ts_cell;
	private final long vce_bound;

	public VersionConstraintElement(CompoundKey ck, ITimestampedCell ts_cell, long bound)
	{
		this.vce_ck = ck;
		this.vce_ts_cell = ts_cell;
		this.vce_bound = bound;
	}

	public CompoundKey getVceCk()
	{
		return vce_ck;
	}

	public ITimestampedCell getVceTsCell()
	{
		return this.vce_ts_cell;
	}

	public long getVceBound()
	{
		return vce_bound;
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(this.vce_ck, this.vce_ts_cell, this.vce_bound);
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == this)
			return true;
		if (o == null)
			return false;
		if (!(o.getClass() == this.getClass()))
			return false;

		VersionConstraintElement that = (VersionConstraintElement) o;
		return Objects.equal(this.vce_ck, that.vce_ck) && Objects.equal(this.vce_ts_cell, that.vce_ts_cell)
				&& Objects.equal(this.vce_bound, that.vce_bound);
	}

	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(this)
				.addValue(this.vce_ck)
				.addValue(this.vce_ts_cell)
				.add("bound", this.vce_bound)
				.toString();
	}
}
