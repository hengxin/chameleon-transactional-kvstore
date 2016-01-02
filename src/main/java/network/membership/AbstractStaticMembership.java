package network.membership;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * In this implementation, we load the membership information
 * from pre-defined properties files.
 * 
 * @author hengxin
 * @date Created on 12-03-2015
 */
public abstract class AbstractStaticMembership {

	private final static Logger LOGGER = LoggerFactory.getLogger(AbstractStaticMembership.class);
	
	protected final String file;
	protected final Properties prop = new Properties();
	protected Member self;
	
	public AbstractStaticMembership(String file) {
		this.file = file;
		this.loadProp();
		this.parseMembershipFromProp();
	}
	
	/**
	 * Parse {@link #prop} into {@link Member}s.
	 */
	public abstract void parseMembershipFromProp();
	
	/**
	 * Load {@link #prop} from {@link #file}.
	 * The whole system exits if an error occurs.
	 * <p> See <a href = "http://stackoverflow.com/a/2523252/1833118">Post: getSystemResourceAsStream() returns null@stackoverflow</a> 
	 * for the use of {@code getResourceAsStream()}.
	 */
	protected void loadProp() {
		ClassLoader class_loader = Thread.currentThread().getContextClassLoader();
		try (InputStream is = class_loader.getResourceAsStream(this.file)) {
			this.prop.load(is);
			LOGGER.info("Load the properties file [{}] successfully.", file);
		} catch (NullPointerException | IOException | IllegalArgumentException e) {
			LOGGER.error("Failed to load properties from [{}]. \n [{}]", this.file, e.getCause());
			System.exit(1);
		}
	}

	public Member self() {
		return this.self;
	}
	
}
