package benchmarking.workload.client;

import org.jetbrains.annotations.NotNull;

/**
 * @author hengxin
 * @date 16-9-7
 */
public interface IClientWorkloadGenerator {
    @NotNull ClientWorkload generate();
}
