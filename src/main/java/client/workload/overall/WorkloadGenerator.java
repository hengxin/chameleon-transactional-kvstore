package client.workload.overall;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

import client.workload.client.IClientWorkloadGenerator;

/**
 * @author hengxin
 * @date 16-9-7
 */
public class WorkloadGenerator implements IWorkloadGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkloadGenerator.class);

    private final int mpl;  // multiprogramming level; i.e., number of (concurrent) clients
    private final IClientWorkloadGenerator clientWorkloadGenerator;

    public WorkloadGenerator(int mpl, IClientWorkloadGenerator clientWorkloadGenerator) {
        this.mpl = mpl;
        this.clientWorkloadGenerator = clientWorkloadGenerator;
    }

    public Workload generate() {
        Workload workload = new Workload(mpl);
        IntStream.range(0, mpl)
                .forEach(index -> workload.add(clientWorkloadGenerator.generate()));

        return workload;
    }

}
