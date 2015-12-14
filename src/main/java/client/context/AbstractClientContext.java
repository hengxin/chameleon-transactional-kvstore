package client.context;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.clientlibrary.transaction.RVSITransaction;
import exception.MemberParseException;
import network.membership.AbstractStaticMembership;
import network.membership.ClientMembership;
import network.membership.Member;
import site.AbstractSite;
import site.ISite;

/**
 * Provides context for transaction processing at the client side, including
 * <p>
 * <ul>
 * <li> {@link #master_slaves_stub_map}: a contact list of server nodes; 
 * 	organized as a map of {@link IMaster}s to their individual list of {@link ISlave}s.
 * <li> TODO {@link #tx}: the currently active {@link RVSITransaction}
 * <li> TODO {@link #newTx()} and {@link #endTx()}: life-cycle management of {@link #tx}. 
 * <li> TO BE IMPLEMENTED
 * </ul>
 * 
 * @author hengxin
 * @date Created on 10-28-2015
 */
public abstract class AbstractClientContext
{
	private final static Logger LOGGER = LoggerFactory.getLogger(AbstractClientContext.class);
	
	private final AbstractStaticMembership client_membership; 
	
	protected final Map<ISite, List<ISite>> master_slaves_stub_map;
	
//	private RVSITransaction tx = null;
	
	/**
	 * Constructor with user-specified properties file.
	 * @param file 
	 * 		Path of the properties file.
	 * @throws MemberParseException 
	 */
	public AbstractClientContext(String file) throws MemberParseException
	{
		LOGGER.info("Using the properties file ({}) for {}.", file, this.getClass().getSimpleName());

		this.client_membership = new ClientMembership(file);
		this.master_slaves_stub_map = this.loadRemoteStubs();
	}

	/**
	 * Obtain the remote stubs (for RMI) of the {@link IMaster}s and their individual list of {@link ISlave}s.
	 * 
	 * @return 
	 * 		A map from {@link IMaster}s to their individual list of {@link ISlave}s.
	 * 
	 * @implNote
	 * 		If a master is not available, then all of its slaves are ignored;
	 * 		If a master is available, then some of its slaves may be ignored if unavailable.
	 * 		Please check the log for details.
	 */
	protected Map<ISite, List<ISite>> loadRemoteStubs()
	{
		return ((ClientMembership) this.client_membership).getMasterSlavesMap().entrySet().stream()
			.<Entry<ISite, List<ISite>>>map(master_slaves_entry ->
			{
				Member master = master_slaves_entry.getKey();
				List<Member> slaves = master_slaves_entry.getValue();

				Optional<ISite> master_stub = AbstractSite.parseStub(master);
				
				if(master_stub.isPresent())
				{
					List<ISite> slaves_stub = AbstractSite.parseStubs(slaves);
					LOGGER.info("Client has contacted the master {} and its slaves {}.", master_stub, slaves_stub);
					return new AbstractMap.SimpleImmutableEntry<>(master_stub.get(), slaves_stub);
				}
				else 
				{
					LOGGER.warn("Failed to locate the master: {}. For now I will ignore it and all its slaves: {}. Note that this may cause serious problems later.", master, slaves);
					return null;	// FIXME Using Optional more elegantly.
				}
			})
			.filter(Objects::nonNull)
			.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
	}
	
	public abstract ISite getReadSite();
}