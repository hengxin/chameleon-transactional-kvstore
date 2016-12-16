package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author hengxin
 * @date 16-9-18
 */
public class ScriptUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScriptUtil.class);
    private static ExecutorService execs = Executors.newCachedThreadPool();

    public static Process exec(String[] cmd) {
//        execs.submit( () -> {
            try {
                LOGGER.info(Arrays.toString(cmd));

                Process proc = Runtime.getRuntime().exec(cmd);

                try (BufferedReader brInput = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
                    brInput.lines().forEach(LOGGER::info);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }

                try (BufferedReader brErr = new BufferedReader(new InputStreamReader(proc.getErrorStream()))) {
                    brErr.lines().forEach(LOGGER::error);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }

                return proc;
//                boolean exitVal = proc.waitFor(15, TimeUnit.MINUTES);
//                if (exitVal)
//                    proc.destroy();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
//        });
        return null;
    }
}
