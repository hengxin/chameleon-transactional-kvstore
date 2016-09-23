package benchmarking.workload.rvsi;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import benchmarking.workload.operation.Operation;
import client.clientlibrary.rvsi.rvsimanager.RVSISpecificationManager;
import client.clientlibrary.rvsi.rvsispec.AbstractRVSISpecification;
import client.clientlibrary.rvsi.rvsispec.BVSpecification;
import client.clientlibrary.rvsi.rvsispec.FVSpecification;
import client.clientlibrary.rvsi.rvsispec.SVSpecification;
import kvs.compound.CompoundKey;

/**
 * {@link UniformRVSISpecificationGenerator} uniformly set k1, k2, k3
 * to all data items to be read.
 *
 * @author hengxin
 * @date 16-9-7
 */
public class UniformRVSISpecificationGenerator implements IRVSISpecificationGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(UniformRVSISpecificationGenerator.class);

    private final int k1, k2, k3;
    public UniformRVSISpecificationGenerator(final int k1, final int k2, final int k3) {
        this.k1 = k1;
        this.k2 = k2;
        this.k3 = k3;
    }

    @NotNull
    @Override
    public RVSISpecificationManager generateRVSISpecManager(@NotNull final Set<Operation> ops) {
        HashSet<CompoundKey> cks = ops2cks(ops);

        AbstractRVSISpecification bvSpec = new BVSpecification();
        bvSpec.addSpec(cks, k1);
        AbstractRVSISpecification fvSpec = new FVSpecification();
        fvSpec.addSpec(cks, k2);
        AbstractRVSISpecification svSpec = new SVSpecification();
        svSpec.addSpec(cks, k3);

        RVSISpecificationManager rvsiSpecManager = new RVSISpecificationManager();
        rvsiSpecManager.collect(bvSpec)
                .collect(fvSpec)
                .collect(svSpec);

        return rvsiSpecManager;
    }

    /**
     * Transform a set of {@link Operation}s to a set of {@link CompoundKey}s
     * @param ops   set of {@link Operation}s
     * @return a set of {@link CompoundKey}s
     *
     * @implNote FIXME: using more general Set to replace HashSet
     */
    private HashSet<CompoundKey> ops2cks(@NotNull Set<Operation> ops) {
        return ops.stream()
                .map(Operation::getCK)
                .collect(Collectors.toCollection(HashSet::new));
    }

}
