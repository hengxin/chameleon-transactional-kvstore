package client.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

import javax.annotation.Nonnull;

import client.clientlibrary.partitioning.IPartitioner;
import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.RVSITransaction;
import client.clientlibrary.transaction.ToCommitTransaction;
import context.AbstractContext;
import kvs.compound.CompoundKey;
import network.membership.ReplicationGroup;
import network.membership.StaticMembershipFromProperties;
import rmi.IRMI;
import site.ISite;

/**
 * Provides context for transaction processing at the client side, including
 * <p><ul>
 * <li> {@link #membership}: a collection of {@link ReplicationGroup} maintained
 *    by {@link StaticMembershipFromProperties}
 * <li> TODO {link #tx}: the currently active {@link RVSITransaction}
 * <li> TODO {link #newTx()} and {link #endTx()}: life-cycle management of {link #tx}.
 * <li> TO BE IMPLEMENTED
 * </ul>
 * 
 * @author hengxin
 * @date Created on 10-28-2015
 */
public abstract class AbstractClientContext extends AbstractContext {
	private final static Logger LOGGER = LoggerFactory.getLogger(AbstractClientContext.class);

	protected final static String DEFAULT_CLIENT_PROPERTIES_FILE = "client/membership-client.properties";
	
	protected IPartitioner partitioner;
	protected Optional<ISite> cached_read_site = Optional.empty();	
	
	/**
	 * Constructor with user-specified properties file.
	 * <p> If some master site cannot be parsed from .properties file 
	 * or located via RMI, the whole system exits immediately.
	 * @param file		path of the properties file; it cannot be {@code null}.
	 */
	public AbstractClientContext(@Nonnull String file) {
		LOGGER.info("Using the properties file [{}] for [{}].", file, this.getClass().getSimpleName());
        membership = new StaticMembershipFromProperties(file);
	}

    /**
     * Partition a {@link ToCommitTransaction} into multiple ones,
     * according to the specific {@link #partitioner}.
     * @param tx  {@link ToCommitTransaction} to partition
     * @return a map from site index to {@link ToCommitTransaction}
     */
    public Map<Integer, ToCommitTransaction> partition(ToCommitTransaction tx) {
        return tx.partition(partitioner, membership.getReplGrpNo());
    }

    public Map<Integer, VersionConstraintManager> partition(VersionConstraintManager vcm) {
        return vcm.partition(partitioner, membership.getReplGrpNo());
    }

	/**
	 * Return a master site who holds value(s) of the specified key.
	 * @param ck	{@link CompoundKey} key
	 * @return		the master {@link IRMI} holding @param ck
	 * @implNote 	This implementation requires and utilizes the {@link #partitioner}
	 * 	specified by subclasses of this {@link AbstractClientContext}. If you don't want
	 *  to rely on {@link IPartitioner}, you can override this method.
	 */
	public ISite getMasterFor(CompoundKey ck) {
		int index = partitioner.locateSiteIndexFor(ck, membership.getReplGrpNo());
		return getMaster(index);
	}

	/**
	 * Return a site who holds value(s) of the specified key.
	 * @param ck	{@link CompoundKey} key
	 * @return		an {@link IRMI} holding @param ck
	 * @implNote	In principle, the client is free to contact <i>any</i> site to read.
	 * 	In this particular implementation, it prefers an already cached slave. 
	 * 
	 *  <p>This implementation requires and utilizes the {@link #partitioner} 
	 * 	specified by subclasses of this {@link AbstractClientContext}. If you don't want
	 *  to rely on {@link IPartitioner}, you can override this method.
	 */
	public ISite getReadSite(CompoundKey ck) {
		return cached_read_site.orElseGet(() -> {
			int index = partitioner.locateSiteIndexFor(ck, membership.getReplGrpNo());
			ISite read_site = membership.getReplGrp(index).getSiteForRead();
			this.cached_read_site = Optional.of(read_site);
			return read_site;
		});
	}
	
	/**
	 * Returns the master site of the {@link ReplicationGroup} with specified replGrpId.
     *
	 * @param replGrpId id of the {@link ReplicationGroup}
	 * @return	the master site of the {@link ReplicationGroup} with @param replGrpId
	 */
	public ISite getMaster(int replGrpId) {
		return membership.getMaster(replGrpId);
	}

}