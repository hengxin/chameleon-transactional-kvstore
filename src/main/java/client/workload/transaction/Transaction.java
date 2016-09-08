package client.workload.transaction;

import com.google.common.base.MoreObjects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import client.workload.operation.Operation;

/**
 * @author hengxin
 * @date 16-9-7
 */
public class Transaction {
    private static final Logger LOGGER = LoggerFactory.getLogger(Transaction.class);

    private final List<Operation> ops;

    public Transaction() {
        ops = new ArrayList<>();
    }

    public Transaction(int capacity) {
        ops = new ArrayList<>(capacity);
    }

    public void addOp(Operation op) {
        ops.add(op);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("list of operations", ops)
                .toString();
    }

}
