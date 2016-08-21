package main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import client.clientlibrary.partitioning.ConsistentHashingDynamicPartitioner;
import client.clientlibrary.rvsi.rvsispec.AbstractRVSISpecification;
import client.clientlibrary.rvsi.rvsispec.BVSpecification;
import client.clientlibrary.rvsi.rvsispec.FVSpecification;
import client.clientlibrary.transaction.ITransaction;
import client.clientlibrary.transaction.RVSITransaction;
import client.context.AbstractClientContext;
import client.context.ClientContextMultiMaster;
import client.context.ClientContextSingleMaster;
import exception.transaction.TransactionBeginException;
import exception.transaction.TransactionEndException;
import exception.transaction.TransactionReadException;
import kvs.component.Cell;
import kvs.component.Column;
import kvs.component.Row;
import kvs.component.Timestamp;
import kvs.compound.CompoundKey;
import kvs.compound.ITimestampedCell;
import twopc.coordinator.RVSI2PCPhaserCoordinator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Main class at the client side. 
 * It tests {@link AbstractClientContext}, {@link RVSITransaction}
 * and {@link RVSI2PCPhaserCoordinator}
 * in both the single-master and the multi-master settings.
 * 
 * @author hengxin
 * @date Created on 12-05-2015
 */
public class ClientMainTest {
	private final static Logger LOGGER = LoggerFactory.getLogger(ClientMainTest.class);
    enum Mode {SingleMasterMode, MultiMasterMode};

    private void testTx(Mode mode) throws TransactionBeginException {
        AbstractClientContext cctx;
        switch (mode) {
            case SingleMasterMode:
                cctx = new ClientContextSingleMaster();
                break;
            case MultiMasterMode:
                cctx = new ClientContextMultiMaster(ConsistentHashingDynamicPartitioner.INSTANCE);
                break;
            default:
                throw new IllegalArgumentException(
                        String.format("No such test mode: [%s].", mode));
        }

		// create transaction
		ITransaction tx = new RVSITransaction(cctx);

		// begin
		assertTrue("Transaction does not begin successfully.", tx.begin());
		assertEquals("Start-timestamp has not been assigned correctly.", new Timestamp(0L), ((RVSITransaction) tx).getSts());

		// read
		Row r = new Row("R");
		Column c = new Column("C");
        CompoundKey ck = new CompoundKey(r, c);

        Row r1 = new Row("R1");
        Column c1 = new Column("C1");
        CompoundKey ck1 = new CompoundKey(r1, c1);

		try {
			ITimestampedCell tsCell = tx.read(r, c);
			LOGGER.info("Read {} from {} + {}.", tsCell, r, c);

            ITimestampedCell tsCell1 = tx.read(r1, c1);
            LOGGER.info("Read {} from {} + {}.", tsCell1, r1, c1);
		} catch (TransactionReadException tre) {
			LOGGER.error(tre.getMessage(), tre.getCause());
			System.exit(1);
		}

		// write
		tx.write(r, c, new Cell("RC"));
		LOGGER.info("Write {} to {} + {}.", "RC", r, c);

        tx.write(r1, c1, new Cell("R1C1"));
        LOGGER.info("Write {} to {} + {}.", "R1C1", r1, c1);

        /**
         * Specifying {@link AbstractRVSISpecification}
         */
        AbstractRVSISpecification bv = new BVSpecification();
        HashSet<CompoundKey> ckSet4BV = Stream.of(ck, ck1)
                .collect(Collectors.toCollection(HashSet::new));
        bv.addSpec(ckSet4BV, 1);

        AbstractRVSISpecification fv = new FVSpecification();
        HashSet<CompoundKey> ckSet4FV = Stream.of(ck1)
                .collect(Collectors.toCollection(HashSet::new));
        fv.addSpec(ckSet4FV, 2);

//        AbstractRVSISpecification sv = new SVSpecification();
        ((RVSITransaction) tx).collectRVSISpecification(bv);
        ((RVSITransaction) tx).collectRVSISpecification(fv);

		// end
		try {
			if (tx.end())
                LOGGER.info("Committed.");
            else
                LOGGER.info("Aborted.");
		} catch (TransactionEndException tee) {
			LOGGER.error(tee.getMessage(), tee.getCause());
			System.exit(1);
		}
    }

	public static void main(String[] args) throws TransactionBeginException {
//	    new ClientMainTest().testTx(Mode.SingleMasterMode);
        new ClientMainTest().testTx(Mode.MultiMasterMode);
	}

}
