package benchmarking.workload.operation;

/**
 * @author hengxin
 * @date 16-9-8
 */
public interface IOperationTypeGenerator {
    enum OpType {READ, WRITE};
    OpType generate();
}
