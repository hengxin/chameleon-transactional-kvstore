package network.membership;

import java.io.FileInputStream;
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
	
	protected final Properties prop = new Properties();
	
	public AbstractStaticMembership(String file)
	{
		this.load(file);
	}

	/**
	 * Load the properties file
	 * @param file properties file path
	 */
	private void load(String file)
	{
		try
		{
			InputStream is = new FileInputStream(file);
//			InputStream is = ClassLoader.getSystemResourceAsStream(file); 
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
	
	protected final Member parse(String member)
	{
		String[] parts = member.split(":|;");
		
		String addr_ip;
		int addr_port;
		String rmi_registry_name;
		int rmi_registry_port;

		try
		{
			addr_ip = parts[0];
			addr_port = Integer.parseInt(parts[1]);
			rmi_registry_name = parts[2];
			rmi_registry_port = Integer.parseInt(parts[3]);

			return new Member(addr_ip, addr_port, rmi_registry_name, rmi_registry_port);
		} catch (NullPointerException npe)
		{
			LOGGER.error("This value ({}) in properties file is ill-formated. The details are: {}.", member, npe);
			return null;
		} catch (NumberFormatException nfe)
		{
			LOGGER.error("This value ({}) in properties file is ill-formated. The details are: {}.", member, nfe);
			return null;
		}
	}
}
