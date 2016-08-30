package client.context;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import client.clientlibrary.partitioning.IPartitioner;
import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.RVSITransaction;
import client.clientlibrary.transaction.ToCommitTransaction;
import context.AbstractContext;
import kvs.compound.CompoundKey;
import membership.coordinator.CoordinatorMembership;
import membership.site.ReplicationGroup;
import membership.site.StaticSiteMembershipFromProperties;
import rmi.IRMI;
import site.ISite;
import timing.TimestampOracleMembership;
import twopc.coordinator.Abstract2PCCoordinator;

/**
 * Provides context for transaction processing at the client side, including
 * <p><ul>
 * <li> {@link #membership}: a collection of {@link ReplicationGroup} maintained
 *    by {@link StaticSiteMembershipFromProperties}
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
    @Language("Properties")
    public final static String DEFAULT_SITE_PROPERTIES_FILE = "client/site.properties";
    @Language("Properties")
    protected final static String DEFAULT_COORD_FACTORY_PROPERTIES_FILE = "client/cf.properties";
    @Language("Properties")
    public final static String DEFAULT_TO_PROPERTIES_FILE = "timing/to.properties";

	protected IPartitioner partitioner;
	protected transient Optional<ISite> cached_read_site = Optional.empty();
	
	/**
	 * Constructor with user-specified properties file.
	 * <p> If some master site cannot be parsed from .properties file 
	 * or located via RMI, the whole system exits immediately.
	 * @param siteProperties	path of the properties file; it cannot be {@code null}.
     * @param cfProperties  path of the cf properties file; it cannot be {@code null}
     * @param toProperties  path of the to properties file; it cannot be {@code null}
     *    TODO: default files
	 */
	public AbstractClientContext(@NotNull String siteProperties,
                                 @NotNull String cfProperties,
                                 @NotNull String toProperties) {
        LOGGER.info("Using site properties file [{}], cf properties file [{}], and to properties file [{}] for [{}].",
                siteProperties, cfProperties, toProperties, this.getClass().getSimpleName());
        membership = new StaticSiteMembershipFromProperties(siteProperties);
        coordMembership = new CoordinatorMembership(cfProperties);
        to = new TimestampOracleMembership(toProperties).locateTO();
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
     * Get the coordinator for committing the transaction {@code tx}
     * @param tx {@link ToCommitTransaction} to commit
     * @return an {@link Abstract2PCCoordinator}
     */
    public Abstract2PCCoordinator getCoord(ToCommitTransaction tx) {
        Set<Integer> masterIds = partition(tx).keySet();
        int size = masterIds.size();
        int coordId = masterIds.toArray(new Integer[size])[new Random().nextInt(size)];
        return coordMembership.getCoord(coordId, this);  // FIXME "this" contains too much stuff
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
			cached_read_site = Optional.of(read_site);
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