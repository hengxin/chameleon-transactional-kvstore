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
	
	private Member self;
	private Member master;
	
	public SlaveMembership(String file)
	{
		super(file);
	}

	@Override
	public void loadMembershipFromProp()
	{
		this.self = Member.parseMember(super.prop.getProperty(SlaveMembership.SELF));
		this.master = Member.parseMember(super.prop.getProperty(SlaveMembership.MASTER));
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
