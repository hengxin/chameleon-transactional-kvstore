package benchmarking.logs;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import benchmarking.workload.WorkloadUtil.WorkloadParams;
import utils.LogUtil;

import static utils.LogUtil.LOG_FILE_EXTENSION;
import static utils.LogUtil.LOG_FILE_PREFIX;
import static utils.LogUtil.LOG_FILE_SEP;
import static utils.LogUtil.extractParamVal;

/**
 * {@Log} represents a log file.
 *
 * Created by hengxin on 16-9-25.
 */
public class Log implements ILogSplitter {
    private static final Logger LOGGER = LoggerFactory.getLogger(Log.class);

    private final String logFile;
    private final Set<String> logSplitFiles = new HashSet<>();

    public Log(String logFile) { this.logFile = logFile; }

    /**
     * Split a whole log record into several ones, according to {@code param}.
     * @param param split according to this <code>param</code>
     * @return file names of split logs
     */
    @Override
    public Set<String> split(WorkloadParams param) {
        try {
            return Files.readLines(new File(logFile), Charsets.UTF_8).stream()
                    .filter(LogUtil::containsData)
                    .map(line -> split(line, param))
                    .collect(Collectors.toSet());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return Collections.emptySet();
    }

    /**
     * Split a line.
     * @param line  log line to split
     * @param param to this {@code param} the {@code line} is split
     * @return  file name for split log
     */
    private String split(String line, WorkloadParams param) {
        String paramVal = extractParamVal(line, param);
        String splitFile = new File(logFile).getParent() + File.separator +
                LOG_FILE_PREFIX + LOG_FILE_SEP + param.param() + LOG_FILE_SEP + paramVal + LOG_FILE_EXTENSION;

        try {
            File file = new File(splitFile);
            if (! logSplitFiles.contains(splitFile)) {
                file.createNewFile();
                Files.write("", file, Charsets.UTF_8);

                logSplitFiles.add(splitFile);
            }

            Files.append(line + System.lineSeparator(), file, Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return splitFile;
    }

}
