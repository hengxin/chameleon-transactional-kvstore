package network.membership;

/**
 * A slave needs to know itself and <i>its</i> master (not all the masters). 
 * 
 * @author hengxin
 * @date Created on 12-03-2015
 */
public final class SlaveMembership extends AbstractStaticMembership
{
	private final static String SELF = "self";
	private final static String MASTER = "master";
	
	private final Member self;
	private final Member master;
	
	public SlaveMembership(String file)
	{
		super(file);
		
		this.self = super.parseMember(super.prop.getProperty(SlaveMembership.SELF));
		this.master = super.parseMember(super.prop.getProperty(SlaveMembership.MASTER));
	}

	public Member getSelf()
	{
		return self;
	}

	public Member getMaster()
	{
		return master;
	}
}
