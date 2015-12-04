package network.membership;

import java.io.FileNotFoundException;
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
	
	public AbstractStaticMembership(String file)
	{
		this.file = file;
	}

	public void loadMembership()
	{
		this.loadProp();
		this.loadMembershipFromProp();
	}
	
	public abstract void loadMembershipFromProp();
	
	/**
	 * Load the .properties file.
	 */
	protected void loadProp()
	{
		try
		{
//			InputStream is = new FileInputStream(this.file);
			InputStream is = ClassLoader.getSystemResourceAsStream(this.file); 
			this.prop.load(is);
			is.close();
		} catch (FileNotFoundException fnfe)
		{
			LOGGER.error("File {} not found.", file);
			System.exit(1);
		} catch (IOException ioe)
		{
			LOGGER.error("Fails to load the {} file. The details are: {}", file, ioe);
			System.exit(1);
		}
	}
}
