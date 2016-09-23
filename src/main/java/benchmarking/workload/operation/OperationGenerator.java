package benchmarking.workload.operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.NoSuchElementException;

import benchmarking.workload.keyspace.IKeySpace;
import benchmarking.workload.operation.IOperationTypeGenerator.OpType;

/**
 * {@link OperationGenerator} is responsible for
 * generating read/write operations over a keyspace.
 *
 * @author hengxin
 * @date 16-9-7
 */
public class OperationGenerator implements IOperationGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(OperationGenerator.class);

    private final IKeySpace keySpace;
    private final IOperationTypeGenerator opTypeSequenceGenerator;
    private final IKeyGenerator rowKeyGenerator;
    private final IKeyGenerator columnKeyGenerator;

    public OperationGenerator(IKeySpace keySpace,
                              IOperationTypeGenerator opTypeSequenceGenerator,
                              IKeyGenerator rowKeyGenerator,
                              IKeyGenerator columnKeyGenerator) {
        this.keySpace = keySpace;
        this.opTypeSequenceGenerator = opTypeSequenceGenerator;
        this.rowKeyGenerator = rowKeyGenerator;
        this.columnKeyGenerator = columnKeyGenerator;
    }

    @Override
    public Operation generate() {
        OpType opType = opTypeSequenceGenerator.generate();
        String rowKey = rowKeyGenerator.generate();
        String colKey = columnKeyGenerator.generate();

        switch (opType) {
            case READ:
                return new ReadOperation(rowKey, colKey);
            case WRITE:
                return new WriteOperation(rowKey, colKey,
                        WithPrefixAndSequenceValueGenerator.next(rowKey, colKey));
            default:
                throw new NoSuchElementException(String.format("No such operation type: %s.", opType));
        }
    }

}
