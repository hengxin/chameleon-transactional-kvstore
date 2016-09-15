package main;

import org.jetbrains.annotations.NotNull;

import timing.CentralizedTimestampOracle;

/**
 * @author hengxin
 * @date 16-8-29
 */
public class CentralizedTimestampOracleMainTest {
    public static void main(@NotNull String[] args) {
        if (args.length == 1)
            new CentralizedTimestampOracle(args[0]);
        else
            new CentralizedTimestampOracle();
    }
}
