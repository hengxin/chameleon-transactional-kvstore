package timing;

import org.intellij.lang.annotations.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import membership.site.Member;
import rmi.IRMI;
import rmi.RMIUtil;
import util.PropertiesUtil;

/**
 * A simple centralized timestamp oracle.
 * The timestamp sequence starts from 0.
 * @author hengxin
 * @date Created on Dec 27, 2015
 */
public class CentralizedTimestampOracle implements ITimestampOracle, IRMI {
    private static final Logger LOGGER = LoggerFactory.getLogger(CentralizedTimestampOracle.class);
    @Language("Properties")
    private static final String DEFAULT_TO_PROPERTIES = "timing/to.properties";

    private final AtomicInteger ts = new AtomicInteger();
    private Member self;

    public CentralizedTimestampOracle() { this(DEFAULT_TO_PROPERTIES); }

    public CentralizedTimestampOracle(String cfProperties) {
        try {
            Properties prop = PropertiesUtil.load(cfProperties);
            Set<String> idStrs = prop.stringPropertyNames();
            String idStr = idStrs.toArray(new String[idStrs.size()])[0];
            self = Member.parseMember(prop.getProperty(idStr)).get();
            export();
        } catch (IOException ioe) {
            LOGGER.error("Failed to parse [{}] from properties [{}]", this.getClass().getSimpleName(), cfProperties);
        }
    }

	@Override
	public int get() throws RemoteException {
	    return ts.getAndIncrement();
	}

    @Override
    public void export() {
        RMIUtil.export(this, self.getHost(), self.getPort(), self.getRmiRegistryName());
    }

    @Override
    public void reclaim() {

    }

}
