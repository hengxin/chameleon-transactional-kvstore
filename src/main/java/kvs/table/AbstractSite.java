package kvs.table;

import jms.AbstractJMSParticipant;

/**
 * An {@link AbstractSite} holds an {@link AbstractTable} 
 * and acts as an {@link AbstractJMSParticipant}.
 * 
 * <p> Specifically, an {@link IMaster} holds a {@link MasterTable}
 * and acts as an {@link JMSCommitLogPublisher}, while an {@link ISlave}
 * holds a {@link SlaveTable} and acts as an {@link JMSCommitLogSubscriber}.
 *  
 * @author hengxin
 * @date Created on 11-25-2015
 */
public abstract class AbstractSite
{
	protected AbstractTable table;
	protected AbstractJMSParticipant jmser;
	
	public void registerAsJMSParticipant(AbstractJMSParticipant jmser)
	{
		this.jmser = jmser;
	}
}
