package master;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.beust.jcommander.JCommander;

/**
 * Test the parameters parser of {@link JCommander}
 * used in {@link MasterConfig}.
 * @author hengxin
 * @date Created on Dec 26, 2015
 */
public class MasterConfigTest
{

	@Before
	public void setUp() throws Exception
	{
	}

	@Test
	public void test()
	{
		MasterConfig config = new MasterConfig();
		String[] args = {"-capacity", "100"};
		new JCommander(config, args);
		
		assertEquals("JCommander does not work.", 100, config.TABLE_CAPACITY);
	}

}
