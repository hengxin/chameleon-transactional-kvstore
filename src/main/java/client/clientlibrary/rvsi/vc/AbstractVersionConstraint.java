package client.clientlibrary.rvsi.vc;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * Each {@link AbstractVersionConstraint} consists of a list of {@link VCEntry}s.
 * 
 * @author hengxin
 * @date Created on 11-16-2015
 */
public abstract class AbstractVersionConstraint
{
	private final List<VCEntry> vc_entry_list;
	
	public AbstractVersionConstraint(List<VCEntry> vc_element_list)
	{
		this.vc_entry_list = vc_element_list;
	}
	
	public abstract boolean check();
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(this.vc_entry_list);
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o == this)
			return true;
		if(o == null)
			return false;
		if(! (o.getClass() == this.getClass()))
			return false;
		
		AbstractVersionConstraint that = (AbstractVersionConstraint) o;
		// isEqualCollection() (on List) requires its element class override hashCode() and equals().
		return CollectionUtils.isEqualCollection(this.vc_entry_list, that.vc_entry_list);
	}
	
	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(this)
				.add("VC_Entry_List", this.vc_entry_list)
				.toString();
	}
}
