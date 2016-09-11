package benchmarking.workload.overall;

import com.google.common.base.MoreObjects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import benchmarking.workload.client.ClientWorkload;

/**
 * {@link Workload} is a collection of {@link ClientWorkload}.
 *
 * @author hengxin
 * @date 16-9-8
 */
public class Workload {
    private static final Logger LOGGER = LoggerFactory.getLogger(Workload.class);

    private final int numberOfClients;
    private final List<ClientWorkload> clientWorkloads;

    public Workload(final int numberOfClients) {
        this.numberOfClients = numberOfClients;
        clientWorkloads = new ArrayList<>(this.numberOfClients);
    }

    public void add(ClientWorkload clientWorkload) {
        clientWorkloads.add(clientWorkload);
    }

    public List<ClientWorkload> getClientWorkloads() {
       return clientWorkloads;
    }

    public int getNumberOfClients() { return numberOfClients; }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("list of client workload", clientWorkloads)
                .toString();
    }

}
