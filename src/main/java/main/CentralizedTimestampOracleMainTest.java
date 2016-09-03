package main;

import timing.CentralizedTimestampOracle;

/**
 * @author hengxin
 * @date 16-8-29
 */
public class CentralizedTimestampOracleMainTest {
    public static void main(String[] args) {
        if (args.length == 1)
            new CentralizedTimestampOracle(args[0]);
        else
            new CentralizedTimestampOracle();
    }
}
