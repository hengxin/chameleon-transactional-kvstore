package benchmarking.executor;

import java.util.Properties;

import client.context.AbstractClientContext;

/**
 * @author hengxin
 * @date 16-9-17
 */
public class ExeBenchmarking {
    private final Properties workloadProp;
    private final AbstractClientContext cctx;

    public ExeBenchmarking(Properties workloadProp, AbstractClientContext cctx) {
        this.workloadProp = workloadProp;
        this.cctx = cctx;
    }
}
