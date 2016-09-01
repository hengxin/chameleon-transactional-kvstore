package main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exception.SiteException;
import master.MasterLauncher;

/**
 * Main class at the master side, 
 * in the <i>single-master-multiple-slaves</i> setting.
 * It simply launches master.
 * 
 * @author hengxin
 * @date Created on 12-05-2015
 */
public class MasterMainTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MasterMainTest.class);

	public static void main(String[] args) {
		try {
		    if (args.length == 0)
		        new MasterLauncher();
            else if (args.length == 2)
                new MasterLauncher(args[0], args[1]);
            else {
                LOGGER.error("Parameters for site.properties and sa.properties.");
                System.exit(1);
            }
		} catch (SiteException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}
}
