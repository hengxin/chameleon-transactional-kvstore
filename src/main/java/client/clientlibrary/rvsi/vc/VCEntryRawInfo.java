package client.clientlibrary.rvsi.vc;

import java.util.Map;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import kvs.component.Ordinal;
import kvs.component.Timestamp;
import kvs.compound.CompoundKey;
import kvs.compound.ITimestampedCell;

/**
 * Raw info for constructing {@link VCEntry}.
 * <p>
 * It consists of three ingredients (yet six fields):
 * <p><ul>
 * <li> {@link #vce_info_ck} and 
 * <li> {@link #vce_info_tscell} identify the "variable" to be constrained
 * <li> {@link #vce_info_ck_optional} and
 * <li> {@link #vce_info_tscell_optional} identify an {@link Timestamp} for 
 *   the variable above to be checked against. 
 *   Note that this is only needed for {@link SVVersionConstraint}.
 * <li> the staleness bound allowed
 * <li> {@link #tmp_ck_tscell_map} temporarily holds values for generating the ultimate
 *   {@link VCEntry} later (for {@link SVVersionConstraint}).
 * </ul><p>
 * 
 * @author hengxin
 * @date Created on 11-16-2015
 */
public class VCEntryRawInfo
{
	private final CompoundKey vce_info_ck;
	private final ITimestampedCell vce_info_tscell;
	private final CompoundKey vce_info_ck_optional;
	private final ITimestampedCell vce_info_tscell_optional;
	private final long vce_info_bound;

	private final Map<CompoundKey, ITimestampedCell> tmp_ck_tscell_map;
	
	/**
	 * Constructor without {@link #vce_info_ck_optional}, {@link #vce_info_tscell_optional},
	 * or {@link #tmp_ck_tscell_map}.
	 * Prepared for constructing {@link VCEntry} for {@link BVVersionConstraint} or {@link FVVersionConstraint}.
	 */
	public VCEntryRawInfo(final CompoundKey ck, final ITimestampedCell ts_cell, final long bound)
	{
		this.vce_info_ck = ck;
		this.vce_info_tscell = ts_cell;
		this.vce_info_bound = bound;
		
		this.vce_info_ck_optional = null;
		this.vce_info_tscell_optional = null;
		
		this.tmp_ck_tscell_map = null;
	}

	/**
	 * Prepared for constructing {@link VCEntry} for {@link SVVersionConstraint}. 
	 */
	public VCEntryRawInfo(final CompoundKey ck, final ITimestampedCell ts_cell, 
			final CompoundKey ck_optional, final ITimestampedCell ts_cell_optional, final long bound)
	{
		this.vce_info_ck = ck;
		this.vce_info_tscell = ts_cell;
		this.vce_info_ck_optional = ck_optional;
		this.vce_info_tscell_optional = ts_cell_optional;
		this.vce_info_bound = bound;
		
		this.tmp_ck_tscell_map = null;
	}
	
	/**
	 * This constructor is used as a temporary holder; 
	 * prepared for constructing the ultimate {@link VCEntryRawInfo} for {@link SVVersionConstraint}. 
	 * 
	 * <p>
	 * This constructor is called at {@link SVSpecification#extractVCEntryRawInfo(QueryResults)}
	 * 
	 * @param ck_tscell_map a map of {@link CompoundKey} to {@link ITimestampedCell}
	 * @param bound staleness bound allowed
	 */
	public VCEntryRawInfo(final Map<CompoundKey, ITimestampedCell> ck_tscell_map, final long bound)
	{
		this.tmp_ck_tscell_map = ck_tscell_map;
		this.vce_info_bound = bound;
		
		this.vce_info_ck = null;
		this.vce_info_tscell = null;
		this.vce_info_ck_optional = null;
		this.vce_info_tscell_optional = null;
	}
	
	public CompoundKey getVceInfoCk()
	{
		return vce_info_ck;
	}

	public ITimestampedCell getVceInfoTscell()
	{
		return this.vce_info_tscell;
	}

	public Ordinal getVceInfoTsCellOrd()
	{
		return this.vce_info_tscell.getOrdinal();
	}
	
	public CompoundKey getVceInfoCkOptional()
	{
		return this.vce_info_ck_optional;
	}
	
	public ITimestampedCell getVceInfoTscellOptional()
	{
		return this.vce_info_tscell_optional;
	}
	
	public long getVceInfoBound()
	{
		return vce_info_bound;
	}

	public Map<CompoundKey, ITimestampedCell> getTmpCkTscellMap()
	{
		return tmp_ck_tscell_map;
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(this.vce_info_ck, this.vce_info_tscell, 
				this.vce_info_ck_optional, this.vce_info_tscell_optional, this.vce_info_bound);
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
		return Objects.equal(this.vce_info_ck, that.vce_info_ck) && Objects.equal(this.vce_info_tscell, that.vce_info_tscell)
				&& Objects.equal(this.vce_info_ck_optional, that.vce_info_ck_optional) && Objects.equal(this.vce_info_tscell_optional, that.vce_info_tscell_optional)
				&& Objects.equal(this.vce_info_bound, that.vce_info_bound);
	}

	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(this)
				.addValue(this.vce_info_ck).addValue(this.vce_info_tscell)
				.addValue(this.vce_info_ck_optional).addValue(this.vce_info_tscell_optional)
				.add("bound", this.vce_info_bound)
				.toString();
	}
}
