package network.membership;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exception.MemberParseException;
import exception.MembershipConfigException;

/**
 * In this implementation, we load the membership information
 * from pre-defined properties files.
 * 
 * @author hengxin
 * @date Created on 12-03-2015
 */
public abstract class AbstractStaticMembership
{
	private final static Logger LOGGER = LoggerFactory.getLogger(AbstractStaticMembership.class);
	
	protected final String file;
	protected final Properties prop = new Properties();
	protected Member self;
	
	public AbstractStaticMembership(String file) throws MemberParseException
	{
		this.file = file;
		this.loadProp();
		this.loadMembershipFromProp();
	}
	
	/**
	 * @throws MemberParseException
	 * 		Thrown if an error occurs during parsing {@link Member}.
	 */
	public abstract void loadMembershipFromProp() throws MemberParseException;
	
	public Member self()
	{
		return this.self;
	}
	
	/**
	 * Load the .properties file.
	 * <p>
	 * See <a href = "http://stackoverflow.com/a/2523252/1833118">Post: getSystemResourceAsStream() returns null @ StackOverflow</a> 
	 * for the use of {@code getResourceAsStream()}.
	 */
	protected Properties loadProp()
	{
		ClassLoader class_loader = Thread.currentThread().getContextClassLoader();
		try (InputStream is = class_loader.getResourceAsStream(this.file)) 
		{
			this.prop.load(is);
			LOGGER.info("Load the properties file ({}) successfully.", file);
		} catch (NullPointerException npe)
		{
			LOGGER.error("The properties file ({}) cannot be found and loaded. \n {}", file, npe);
			System.exit(1);
		} catch (IllegalArgumentException | IOException ie)
		{
			LOGGER.error("An error occurred when reading from the properties {} file. \n {}", file, ie);
			System.exit(1);
		}
		
		return this.prop;
	}
}
