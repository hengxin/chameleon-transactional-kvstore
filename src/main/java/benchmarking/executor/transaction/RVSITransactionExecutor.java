package benchmarking.executor.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import benchmarking.workload.operation.Operation;
import benchmarking.workload.operation.ReadOperation;
import benchmarking.workload.transaction.Transaction;
import client.clientlibrary.transaction.ITransaction;
import client.clientlibrary.transaction.RVSITransaction;
import client.context.AbstractClientContext;
import exception.transaction.TransactionBeginException;
import exception.transaction.TransactionEndException;
import exception.transaction.TransactionReadException;
import kvs.component.Cell;
import kvs.component.Column;
import kvs.component.Row;
import twopc.TransactionCommitResult;

/**
 * {@link RVSITransactionExecutor} wraps {@link Transaction}
 * as an {@link RVSITransactionExecutor} and executes it.
 *
 * @author hengxin
 * @date 16-9-7
 */
public class RVSITransactionExecutor implements ITransactionExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(RVSITransactionExecutor.class);

    private final AbstractClientContext cctx;

    public RVSITransactionExecutor(AbstractClientContext cctx) {
        this.cctx = cctx;
    }

    @Override
    public TransactionCommitResult execute(Transaction tx)
            throws TransactionBeginException, TransactionReadException, TransactionEndException {
        ITransaction rvsiTx = new RVSITransaction(cctx);

        rvsiTx.begin();

        tx.getOps().stream()
                .forEachOrdered(op ->
                        executeOp(rvsiTx, op));

        ((RVSITransaction) rvsiTx).setRvsiSpecManager(tx.getRvsiSpecManager());

        return rvsiTx.end();
    }

    private void executeOp(ITransaction tx, Operation op) {
        if (op instanceof ReadOperation)
            tx.read(new Row(op.getRow()), new Column(op.getCol()));
        else
            tx.write(new Row(op.getRow()), new Column(op.getCol()), new Cell(op.getVal()));
    }

}
