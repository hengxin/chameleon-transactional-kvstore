package master;

import com.beust.jcommander.Parameter;

/**
 * Configuration parameters for master.
 * @author hengxin
 * @date Created on Dec 26, 2015
 */
public class MasterConfig {
	@Parameter(names = "-capacity", description = "Number of Versions to Keep")
	public static final int TABLE_CAPACITY = 10;
}
