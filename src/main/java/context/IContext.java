package context;

import network.membership.Member;

/**
 * Context for sites.
 * 
 * @author hengxin
 * @date Created on 12-09-2015
 */
public interface IContext
{
	public abstract Member self();
}
