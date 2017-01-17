package benchmarking.workload.operation;

import org.jetbrains.annotations.NotNull;

/**
 * @author hengxin
 * @date 16-9-7
 */
public interface IOperationGenerator {
    @NotNull Operation generate();
}
