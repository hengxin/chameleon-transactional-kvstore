package benchmarking.workload.transaction;

import com.google.common.base.MoreObjects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import benchmarking.workload.operation.Operation;
import benchmarking.workload.operation.ReadOperation;
import client.clientlibrary.rvsi.rvsimanager.RVSISpecificationManager;

/**
 * @author hengxin
 * @date 16-9-7
 */
public class Transaction {
    private static final Logger LOGGER = LoggerFactory.getLogger(Transaction.class);

    private final List<Operation> ops;
    private RVSISpecificationManager rvsiSpecManager;

    public Transaction() { ops = new ArrayList<>(); }
    public Transaction(int capacity) { ops = new ArrayList<>(capacity); }

    void addOp(Operation op) { ops.add(op); }

    public List<Operation> getOps() { return ops; }
    public RVSISpecificationManager getRvsiSpecManager() { return rvsiSpecManager; }

    Set<Operation> getReadOps() {
        return ops.stream()
                .filter(op -> op instanceof ReadOperation)
                .collect(Collectors.toSet());
    }

    public void setRvsiSpecManager(RVSISpecificationManager rvsiSpecManager) { this.rvsiSpecManager = rvsiSpecManager; }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("list of operations", ops)
                .add("rvsi-manager", rvsiSpecManager)
                .toString();
    }

}
