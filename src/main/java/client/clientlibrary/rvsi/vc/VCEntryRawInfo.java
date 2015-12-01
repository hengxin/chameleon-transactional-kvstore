package client.clientlibrary.rvsi.vc;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import kvs.component.Ordinal;
import kvs.component.Timestamp;
import kvs.compound.CompoundKey;
import kvs.compound.ITimestampedCell;
import kvs.compound.KVItem;

/**
 * Raw info for constructing {@link VCEntry}.
 * <p>
 * It consists of three parts:
 * <p><ul>
 * <li> {@link #vce_info_kv} identify a "key-value" pair to be checked
 * <li> {@link #vce_info_kv_optional} identify another "key-value" pair for 
 *   the "key-value" pair above to be checked against. 
 *   Note that this is only needed for {@link SVVersionConstraint}.
 * <li> the staleness bound allowed
 * </ul><p>
 * 
 * @author hengxin
 * @date Created on 11-16-2015
 */
public class VCEntryRawInfo
{
	private final KVItem vce_info_kv;
	private final KVItem vce_info_kv_optional;
	private final long vce_info_bound;
	
	public VCEntryRawInfo(final KVItem kv, final KVItem kv_optional, final long bound)
	{
		this.vce_info_kv = kv;
		this.vce_info_kv_optional = kv_optional;
		this.vce_info_bound = bound;
	}

	/**
	 * Constructor without {@link #vce_info_kv_optional}.
	 * Prepared for constructing {@link VCEntry} for {@link BVVersionConstraint} or {@link FVVersionConstraint}.
	 */
	public VCEntryRawInfo(final CompoundKey ck, final ITimestampedCell ts_cell, final long bound)
	{
		this.vce_info_kv = new KVItem(ck, ts_cell);
		this.vce_info_bound = bound;
		
		this.vce_info_kv_optional = null;
	}

	/**
	 * Prepared for constructing {@link VCEntry} for {@link SVVersionConstraint}. 
	 */
	public VCEntryRawInfo(final CompoundKey ck, final ITimestampedCell ts_cell, 
			final CompoundKey ck_optional, final ITimestampedCell ts_cell_optional, final long bound)
	{
		this.vce_info_kv = new KVItem(ck, ts_cell);
		this.vce_info_kv_optional = new KVItem(ck_optional, ts_cell_optional);
		this.vce_info_bound = bound;
	}
	
	public CompoundKey getVceInfoCk()
	{
		return this.vce_info_kv.getCk();
	}

	public Ordinal getVceInfoOrd()
	{
		return this.vce_info_kv.getTscell().getOrdinal();
	}
	
	public long getVceInfoBound()
	{
		return vce_info_bound;
	}
	
	public Timestamp getVceInfoTsOptional()
	{
		return this.vce_info_kv_optional.getTscell().getTS();
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(this.vce_info_kv, this.vce_info_kv_optional, this.vce_info_bound);
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
		return Objects.equal(this.vce_info_kv, that.vce_info_kv) && Objects.equal(this.vce_info_kv_optional, that.vce_info_kv_optional)
				&& Objects.equal(this.vce_info_bound, that.vce_info_bound);
	}

	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(this)
				.addValue(this.vce_info_kv)
				.addValue(this.vce_info_kv_optional)
				.add("bound", this.vce_info_bound)
				.toString();
	}
}
