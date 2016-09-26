package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author hengxin
 * @date 16-9-18
 */
public class ScriptUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScriptUtil.class);
    private static ExecutorService exec = Executors.newCachedThreadPool();

    public static void exec(String[] cmd) {
            try {
                Process proc = Runtime.getRuntime().exec(cmd);

                    try (BufferedReader brInput = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
//                        brInput.lines().forEach( line -> {} );
                        String in;
                        while ((in = brInput.readLine()) != null)
                            ;
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }

                            try (BufferedReader brErr = new BufferedReader(new InputStreamReader(proc.getErrorStream()))) {
                                String err;
                                while ((err = brErr.readLine()) != null)
                                    ;
//                    brErr.lines().forEach(LOGGER::info);
                            } catch (IOException ioe) {
                                ioe.printStackTrace();
                            }

                boolean exitVal = proc.waitFor(15, TimeUnit.MINUTES);
                if (exitVal)
                    proc.destroy();
            } catch (IOException | InterruptedException ioie) {
                ioie.printStackTrace();
            }
    }
}
