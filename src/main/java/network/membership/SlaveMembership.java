package network.membership;

/**
 * A slave needs to know itself and <i>its</i> master (not all the masters). 
 * @author hengxin
 * @date Created on 12-03-2015
 */
public class SlaveMembership extends AbstractStaticMembership
{
	private final Member self;
	private final Member master;
	
	public SlaveMembership(String file)
	{
		super(file);
		
		this.self = super.parse(super.prop.getProperty("self"));
		this.master = super.parse(super.prop.getProperty("master"));
	}

}
