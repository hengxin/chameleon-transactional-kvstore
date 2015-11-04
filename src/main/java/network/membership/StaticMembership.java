/**
 * 
 */
package network.membership;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author hengxin
 * @date Created on 02-11-2015
 * 
 * Parse the membership.properties file to obtain the member list 
 * (including the master and the slaves)
 */
public enum StaticMembership implements IMembership
{
	INSTANCE;
	
	private static final String MEMBERSHIP_PROPERTIES_FILE = "membership.properties";
	private Properties properties = new Properties();
	
	private String[] addrs = null;

	/**
	 * parse the membership.properties file
	 */
	private StaticMembership()
	{
		StaticMembership.load(this.properties, StaticMembership.MEMBERSHIP_PROPERTIES_FILE);
		this.addrs = StaticMembership.extractAddrs(this.properties);
	}

	@Override
	public String[] getMemberAddrs()
	{
		return this.addrs;
	}
	
	@Override
	public String getMasterAddr()
	{
		return this.addrs[0];
	}
	
	/**
	 * @param prop {@link Properties}
	 * @return an array of addresses
	 */
	protected static String[] extractAddrs(Properties prop)
	{
		int size = prop.size();
		String[] addrs = new String[size];
		
		for (int i = 0; i < size; i++)
			addrs[i] = prop.getProperty(String.valueOf(i));
		
		return addrs;
	}

	/**
	 * load the .properties file
	 * @param prop {@link Properties} to be filled in
	 * @param file file path
	 */
	protected static void load(Properties prop, String file)
	{
		try
		{
			InputStream is = new FileInputStream(file);
//			InputStream is = ClassLoader.getSystemResourceAsStream(file); 
			prop.load(is);
			is.close();
		} catch (FileNotFoundException fnfe)
		{
			fnfe.printStackTrace();
		} catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
	
}
