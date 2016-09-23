package benchmarking.workload.rvsi;

import java.util.Set;

import benchmarking.workload.operation.Operation;
import client.clientlibrary.rvsi.rvsimanager.RVSISpecificationManager;

/**
 * @author hengxin
 * @date 16-9-10
 */
public interface IRVSISpecificationGenerator {
    RVSISpecificationManager generateRVSISpecManager(final Set<Operation> ops);
}
