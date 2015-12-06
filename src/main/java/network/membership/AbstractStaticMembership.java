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
public abstract class AbstractStaticMembership
{
	private final static Logger LOGGER = LoggerFactory.getLogger(AbstractStaticMembership.class);
	
	private final String file;
	protected final Properties prop = new Properties();
	protected Member self;
	
	public AbstractStaticMembership(String file)
	{
		this.file = file;
		this.loadProp();
		this.loadMembershipFromProp();
	}
	
	public abstract void loadMembershipFromProp();
	
	public Member getSelf()
	{
		return this.self;
	}
	
	/**
	 * Load the .properties file.
	 */
	protected Properties loadProp()
	{
		InputStream is = null;
		try
		{
//			InputStream is = new FileInputStream(this.file);
//			ClassLoader class_loader = Thread.currentThread().getContextClassLoader();
//			is = class_loader.getResourceAsStream(this.file); 
//			is = ClassLoader.getSystemResourceAsStream(this.file);
			is = AbstractStaticMembership.class.getResourceAsStream(this.file);
			this.prop.load(is);
		} catch (NullPointerException npe)
		{
			LOGGER.error("The properties file ({}) cannot be found and loaded. \n {}", file, npe);
			System.exit(1);
//		} catch (FileNotFoundException fnfe)
//		{
//			LOGGER.error("File {} not found.", file);
//			System.exit(1);
		} catch (IOException ioe)
		{
			LOGGER.error("An error occurred when reading from the properties {} file. \n {}", file, ioe);
			System.exit(1);
		} catch (IllegalArgumentException iae)
		{
			LOGGER.error("The input stream obtained from the properties {} file contains a malformed Unicode escape sequence. \n {}", file, iae);
			System.exit(1);
		} finally 
		{
			if(is != null)
			{
				try
				{
					is.close();
				} catch (IOException ioe)
				{
					LOGGER.warn("Failed to close the input stream obtained from the properties {} file. \n {}", file, ioe);
				}
			}
		}
		
		return this.prop;
	}
}
