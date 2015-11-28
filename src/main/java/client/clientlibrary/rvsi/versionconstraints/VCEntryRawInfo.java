package client.clientlibrary.rvsi.versionconstraints;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import kvs.component.Timestamp;
import kvs.compound.CompoundKey;
import kvs.compound.ITimestampedCell;

/**
 * Raw info for constructing {@link VCEntry}.
 * <p>
 * It consists of three ingredients (yet five fields):
 * <p><ul>
 * <li> {@link #vce_info_ck} and 
 * <li> {@link #vce_info_ts_cell} identify the "variable" to be constrained
 * <li> {@link #vce_info_ck_optional} and
 * <li> {@link #vce_info_ts_cell_optional} identify an {@link Timestamp} for 
 *   the variable above to be checked against. 
 *   Note that this is only needed for {@link SVVersionConstraint}.
 * <li> the staleness bound allowed
 * </ul><p>
 * 
 * @author hengxin
 * @date Created on 11-16-2015
 */
public class VCEntryRawInfo
{
	private final CompoundKey vce_info_ck;
	private final ITimestampedCell vce_info_ts_cell;
	private final CompoundKey vce_info_ck_optional;
	private final ITimestampedCell vce_info_ts_cell_optional;
	private final long vce_info_bound;

	/**
	 * Constructor without {@link #vce_info_ck_optional} and {@link #vce_info_ts_cell_optional};
	 * prepared for constructing {@link VCEntry} for {@link BVVersionConstraint} or {@link FVVersionConstraint}.
	 */
	public VCEntryRawInfo(final CompoundKey ck, final ITimestampedCell ts_cell, final long bound)
	{
		this.vce_info_ck = ck;
		this.vce_info_ts_cell = ts_cell;
		this.vce_info_bound = bound;
		
		this.vce_info_ck_optional = null;
		this.vce_info_ts_cell_optional = null;
	}

	/**
	 * Prepared for constructing {@link VCEntry} for {@link SVVersionConstraint}. 
	 */
	public VCEntryRawInfo(final CompoundKey ck, final ITimestampedCell ts_cell, 
			final CompoundKey ck_optional, final ITimestampedCell ts_cell_optional, final long bound)
	{
		this.vce_info_ck = ck;
		this.vce_info_ts_cell = ts_cell;
		this.vce_info_ck_optional = ck_optional;
		this.vce_info_ts_cell_optional = ts_cell_optional;
		this.vce_info_bound = bound;
	}
	
	public CompoundKey getVceInfoCk()
	{
		return vce_info_ck;
	}

	public ITimestampedCell getVceInfoTsCell()
	{
		return this.vce_info_ts_cell;
	}

	public CompoundKey getVceInfoCkOptional()
	{
		return this.vce_info_ck_optional;
	}
	
	public ITimestampedCell getVceInfoTsCellOptional()
	{
		return this.vce_info_ts_cell_optional;
	}
	
	public long getVceInfoBound()
	{
		return vce_info_bound;
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(this.vce_info_ck, this.vce_info_ts_cell, 
				this.vce_info_ck_optional, this.vce_info_ts_cell_optional, this.vce_info_bound);
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

		VCEntryRawInfo that = (VCEntryRawInfo) o;
		return Objects.equal(this.vce_info_ck, that.vce_info_ck) && Objects.equal(this.vce_info_ts_cell, that.vce_info_ts_cell)
				&& Objects.equal(this.vce_info_ck_optional, that.vce_info_ck_optional) && Objects.equal(this.vce_info_ts_cell_optional, that.vce_info_ts_cell_optional)
				&& Objects.equal(this.vce_info_bound, that.vce_info_bound);
	}

	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(this)
				.addValue(this.vce_info_ck).addValue(this.vce_info_ts_cell)
				.addValue(this.vce_info_ck_optional).addValue(this.vce_info_ts_cell_optional)
				.add("bound", this.vce_info_bound)
				.toString();
	}
}
