package client.clientlibrary.rvsi.vc;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import org.jetbrains.annotations.Nullable;

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
 * <li> {@link #vceInfoKV} identify a "key-value" pair to be checked
 * <li> {@link #vceInfoKvOptional} identify another "key-value" pair for
 *   the "key-value" pair above to be checked against. 
 *   Note that this is only needed for {@link SVVersionConstraint}.
 * <li> the staleness bound allowed
 * </ul><p>
 * 
 * @author hengxin
 * @date Created on 11-16-2015
 */
public final class VCEntryRawInfo {
	private final KVItem vceInfoKV;
	@Nullable
    private final KVItem vceInfoKvOptional;
	private final int vceInfoBound;
	
	public VCEntryRawInfo(final KVItem kv, final KVItem kv_optional, final int bound) {
		vceInfoKV = kv;
		vceInfoKvOptional = kv_optional;
		vceInfoBound = bound;
	}

	/**
	 * Constructor without {@link #vceInfoKvOptional}.
	 * Prepared for constructing {@link VCEntry} for {@link BVVersionConstraint} or {@link FVVersionConstraint}.
	 */
	public VCEntryRawInfo(final CompoundKey ck, final ITimestampedCell ts_cell, final int bound) {
		vceInfoKV = new KVItem(ck, ts_cell);
		vceInfoBound = bound;
		
		vceInfoKvOptional = null;
	}

	/**
	 * Prepared for constructing {@link VCEntry} for {@link SVVersionConstraint}. 
	 */
	public VCEntryRawInfo(final CompoundKey ck, final ITimestampedCell ts_cell,
			final CompoundKey ck_optional, final ITimestampedCell ts_cell_optional, final int bound) {
		vceInfoKV = new KVItem(ck, ts_cell);
		vceInfoKvOptional = new KVItem(ck_optional, ts_cell_optional);
		vceInfoBound = bound;
	}
	
	public CompoundKey getVceInfoCk() { return vceInfoKV.getCK(); }
	public Ordinal getVceInfoOrd() { return vceInfoKV.getTsCell().getOrdinal(); }
	public int getVceInfoBound() { return vceInfoBound; }
	public Timestamp getVceInfoTsOptional() { return vceInfoKvOptional.getTsCell().getTS(); }

	@Override
	public int hashCode()
	{
		return Objects.hashCode(vceInfoKV, vceInfoKvOptional, vceInfoBound);
	}

	@Override
	public boolean equals(@Nullable Object o) {
		if (o == this)
			return true;
		if (o == null)
			return false;
		if (!(o.getClass() == this.getClass()))
			return false;

		VCEntryRawInfo that = (VCEntryRawInfo) o;
		return Objects.equal(this.vceInfoKV, that.vceInfoKV) && Objects.equal(this.vceInfoKvOptional, that.vceInfoKvOptional)
				&& Objects.equal(this.vceInfoBound, that.vceInfoBound);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.addValue(vceInfoKV)
				.addValue(vceInfoKvOptional)
				.add("bound", vceInfoBound)
				.toString();
	}
}
