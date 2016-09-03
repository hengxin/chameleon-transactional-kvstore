package main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exception.SiteException;
import slave.SlaveLauncher;

/**
 * Main class at the slave side in the <i>single-master-multiple-slaves</i> setting.
 * 
 * @author hengxin
 * @date Created on 12-05-2015
 */
public class SlaveMainTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(SlaveMainTest.class);

	public static void main(String[] args) {
        try {
            if (args.length == 0)
                new SlaveLauncher();
            else if (args.length == 2)
                new SlaveLauncher(args[0], args[1]);
            else {
                LOGGER.error("Need parameters for site.properties and sa.properties.");
                System.exit(1);
            }
		} catch (SiteException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}
}
