package benchmarking.logs;

import java.util.Set;

import benchmarking.workload.WorkloadUtil.WorkloadParams;

/**
 * {@link ILogSplitter} can split a single log file into multiple ones,
 * according to some field.
 *
 * Created by hengxin on 16-9-25.
 */
public interface ILogSplitter {
    /**
     * Split a single log file into multiple ones.
     *
     * @param param split according to this <code>param</code>
     * @return  a set of log files
     *
     * @implNote This method may write files.
     */
    Set<String> split(WorkloadParams param);
}
