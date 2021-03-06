package client.context;

import org.jetbrains.annotations.NotNull;

import client.clientlibrary.partitioning.IPartitioner;
import conf.SiteConfig;

import static conf.SiteConfig.DEFAULT_CLIENT_COORD_FACTORY_PROPERTIES_FILE;
import static conf.SiteConfig.DEFAULT_CLIENT_SITE_PROPERTIES_FILE;
import static conf.SiteConfig.DEFAULT_TO_PROPERTIES;

/**
 * Provides context for transaction processing at the client side
 * in the <i>multiple-masters</i> setting.
 * 
 * @author hengxin
 * @date Created on 12-04-2015
 */
public class ClientContextMultiMaster extends AbstractClientContext {
    private static final long serialVersionUID = -7753287241266662754L;

    /**
	 * Constructor with the default .properties file: {@value SiteConfig#DEFAULT_CLIENT_SITE_PROPERTIES_FILE}
	 * and user-specified {@link IPartitioner}.
	 * @param partitioner	{@link IPartitioner} for keyspace partition strategy
	 */
	public ClientContextMultiMaster(IPartitioner partitioner) {
		this(DEFAULT_CLIENT_SITE_PROPERTIES_FILE,
                DEFAULT_CLIENT_COORD_FACTORY_PROPERTIES_FILE,
                DEFAULT_TO_PROPERTIES,
                partitioner);
	}
	
	/**
	 * Constructor with user-specified .properties files and {@link IPartitioner}.
     * @param siteProperties properties file for sites
     * @param cfProperties  properties file for coordinator factory
     * @param toProperties  properties file for timestamp oracle
	 * @param partitioner	{@link IPartitioner} for keyspace partition strategy
	 */
	public ClientContextMultiMaster(@NotNull String siteProperties, @NotNull String cfProperties, @NotNull String toProperties,
                                    IPartitioner partitioner) {
		super(siteProperties, cfProperties, toProperties);
		super.partitioner = partitioner;
	}

}
