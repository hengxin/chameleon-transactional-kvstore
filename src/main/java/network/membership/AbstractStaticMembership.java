package network.membership;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

/**
 * In this implementation, we load the membership information
 * from pre-defined properties files.
 * 
 * @author hengxin
 * @date Created on 12-03-2015
 */
@Deprecated
public abstract class AbstractStaticMembership {

	private final static Logger LOGGER = LoggerFactory.getLogger(AbstractStaticMembership.class);
	
//	@Nonnull protected final String file;
//	@Nonnull protected final Properties prop = new Properties();
	@Nonnull protected Member self;
	
	public AbstractStaticMembership(@Nonnull String file) {
//		this.file = file;
//		this.loadProp();
//		this.parseMembershipFromProp();
	}
	
//	/**
//	 * Parse {@link #prop} into {@link Member}s.
//	 */
//	public abstract void parseMembershipFromProp();
	
//	/**
//	 * Load {@link #prop} from {@link #file}.
//	 * The whole system exits if an error occurs.
//	 * <p> See <a href = "http://stackoverflow.com/a/2523252/1833118">Post: getSystemResourceAsStream() returns null@stackoverflow</a>
//	 * for the use of {@code getResourceAsStream()}.
//	 */
//	protected void loadProp() {
//		ClassLoader class_loader = Thread.currentThread().getContextClassLoader();
//		try (InputStream is = class_loader.getResourceAsStream(file)) {
//			this.prop.load(is);
//			LOGGER.info("Load the properties file [{}] successfully.", file);
//		} catch (NullPointerException | IOException | IllegalArgumentException exp) {	// TODO catch Exception???
//			LOGGER.error("Failed to load properties from [{}]. \n [{}]", file, exp);
//			System.exit(1);
//		}
//	}

	public Member self() {
		return this.self;
	}
	
}
