/**
 * 
 */
package kvs.table;

import com.google.common.collect.TreeBasedTable;

import net.jcip.annotations.ThreadSafe;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import client.clientlibrary.transaction.BufferedUpdates;
import client.clientlibrary.transaction.ToCommitTransaction;
import kvs.component.Column;
import kvs.component.Row;
import kvs.component.Timestamp;
import kvs.compound.CompoundKey;
import kvs.compound.ITimestampedCell;
import kvs.compound.TimestampedCell;

/**
 * @author hengxin
 * @date Created: 10-24-2015
 * 
 * <p> In this implementation, the synchronization is achieved by {@link ReentrantReadWriteLock}.
 * <p> TODO using the built-in synchronization mechanism of {@link TreeBasedTable}.
 */
@ThreadSafe
public abstract class AbstractTable {
	private final static Logger LOGGER = LoggerFactory.getLogger(AbstractTable.class);
	
	private final TreeBasedTable<Row, Column, ITimestampedCellStore> table = TreeBasedTable.create();
	
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private final Lock readLock = lock.readLock();
	private final Lock writeLock = lock.writeLock();
	
	/**
	 * lookup a row with a specific row key
	 * @param row {@link Row} key
	 * @return a table row
	 * 
	 * FIXME not implemented yet
	 */
	@NotNull
    public SortedMap<Column, ITimestampedCellStore> getRow(Row row)
            throws NoSuchMethodException {
//		return table.row(row);
        throw new NoSuchMethodException("Not implemented yet.");
	}
	
	/**
	 * lookup the <i>latest</i> {@link ITimestampedCell} indexed by a {@link Row} key (@param row)
	 * and a {@link Column} key (@param col)
	 * 
	 * <b>Note:</b> The result could be "NULL" (the initial value), 
	 * represented by {@link TimestampedCell#TIMESTAMPED_CELL_INIT}.
	 * 
	 * @param row {@link Row} key
	 * @param col {@link Column} key
	 * @return an {@link Optional} wrapper of {@link ITimestampedCell}
	 */
	public ITimestampedCell getTimestampedCell(Row row, Column col) {
		Optional<ITimestampedCellStore> tsCellStore = this.getTimestampedCellStore(row, col);
		return tsCellStore.isPresent() ? tsCellStore.get().get() : TimestampedCell.TIMESTAMPED_CELL_INIT;
	}

    /**
     * Gets the <em>latest</em> preceding {@link ITimestampedCell} with {@link Timestamp} smaller than @param ts,
     * indexed by a {@link CompoundKey}.
     *
     * <b>Note:</b> The result could be "NULL" (the initial value),
     * represented by {@link TimestampedCell#TIMESTAMPED_CELL_INIT}.
     *
     * @param ck {@link CompoundKey} to find
     * @param ts {@link Timestamp} to compare
     * @return an {@link Optional} wrapper of {@link ITimestampedCell}
     */
	public ITimestampedCell getTimestampedCell(@NotNull CompoundKey ck, Timestamp ts) {
	    return getTimestampedCell(ck.getRow(), ck.getCol(), ts);
    }

	/**
	 * Gets the <em>latest</em> preceding {@link ITimestampedCell} with {@link Timestamp} smaller than @param ts, 
	 * indexed by a {@link Row} key (@param row) and a {@link Column} key (@param col).
	 * 
	 * <b>Note:</b> The result could be "NULL" (the initial value), 
	 * represented by {@link TimestampedCell#TIMESTAMPED_CELL_INIT}.
	 * 
	 * @param row {@link Row} key
	 * @param col {@link Column} key
	 * @param ts {@link Timestamp} to compare
	 * @return an {@link Optional} wrapper of {@link ITimestampedCell}
	 */
	public ITimestampedCell getTimestampedCell(Row row, Column col, Timestamp ts) {
		Optional<ITimestampedCellStore> tsCellStore = getTimestampedCellStore(row, col);
		return tsCellStore.isPresent() ? tsCellStore.get().get(ts) : TimestampedCell.TIMESTAMPED_CELL_INIT;
	}
	
	/**
	 * lookup an {@link ITimestampedCellStore} indexed
	 * by a {@link Row} key (@param row) and a {@link Column} key (@param col)
	 * 
	 * <b>Note:</b> The result could be null, represented by <code>Optional.empty()</code>.
	 * 
	 * @param row {@link Row} key
	 * @param col {@link Column} key
	 * @return an {@link Optional} wrapper of {@link ITimestampedCellStore}
	 */
	protected Optional<ITimestampedCellStore> getTimestampedCellStore(Row row, Column col) {
		readLock.lock();
		try {
			return Optional.ofNullable(table.get(row, col));
		} finally {
			readLock.unlock();
		}
	}
	
	/**
	 * Put a row of data.
	 * @param row {@link Row} key
	 * @param col_data_map	data map for each {@link Column}
	 * 
	 * FIXME Not implemented yet!
	 */
	public void put(Row row, Map<Column, ITimestampedCell> col_data_map) {
	}
	
	/**
	 * Apply the {@link ToCommitTransaction} to this {@link #table}
	 * @param tx transaction commit log of type {@link ToCommitTransaction}
	 */
	public void apply(@NotNull ToCommitTransaction tx) {
	    LOGGER.info("Begin: apply tx.");
		this.apply(tx.getCts(), tx.getBufferedUpdates());
        LOGGER.info("End: apply tx.");
	}
	
	/**
	 * Apply all the {@link BufferedUpdates} with timestamp @param cts to this {@link #table}.
	 * @param cts	new {@link Timestamp} for @param buffered_updates
	 * @param bufferedUpdates {@link BufferedUpdates} to be applied
	 */
	public void apply(@NotNull Timestamp cts, @NotNull BufferedUpdates bufferedUpdates) {
	    LOGGER.info("Begin: apply cts and bufferedUpdates.");
		bufferedUpdates.stream().forEach(item ->
							put(item.getCK(), TimestampedCell.replaceTimestamp(cts, item.getTsCell())));
        LOGGER.info("End: apply cts and bufferedUpdates.");
	}
	
	/**
	 * Put data ({@link CompoundKey}, {@link ITimestampedCell}) into {@link #table}.
	 */
	public void put(@NotNull CompoundKey ck, ITimestampedCell tc) {
	    LOGGER.info("Begin: put ck [{}] and tc [{}].", ck, tc);
		put(ck.getRow(), ck.getCol(), tc);
        LOGGER.info("End: put ck [{}] and tc [{}].", ck, tc);
	}
	
	/**
	 * Putting data (row, column, timestamped-cell) into this {@link #table}.
	 * <p> If the {@link ITimestampedCellStore} corresponding to (row, column) does not
	 * exist, then initialize one and put the timestamped-cell into it.
	 * The synchronization mechanism should prevent an {@link ITimestampedCellStore} 
	 * corresponding to the same (row, column) from being initialized twice.
	 * 
	 * @param row {@link Row} key
	 * @param col {@link Column} key
	 * @param tc the {@link ITimestampedCell} to store
	 * 
	 * @implNote
	 * 	Template design pattern. 
	 * 
	 * FIXME Using computeIfAbsent() to avoid initStore()???
	 */
	public void put(@NotNull Row row, @NotNull Column col, ITimestampedCell tc) {
		LOGGER.debug("Begin: put [row: {}, col: {}, tc: {}] into table.", row, col, tc);
		
		Optional<ITimestampedCellStore> tsCellStore = this.getTimestampedCellStore(row, col);
	
		if(tsCellStore.isPresent()) {    // fast path
            LOGGER.debug("Begin fast path: [ItsCellStore {}; tc {}].", tsCellStore.get(), tc);
            tsCellStore.get().put(tc);
            LOGGER.debug("End fast path: [ItsCellStore {}; tc {}].", tsCellStore.get(), tc);
        }
		else {
			writeLock.lock();
			try {
				Optional<ITimestampedCellStore> secondTsCellStore = this.getTimestampedCellStore(row, col);
				if (secondTsCellStore.isPresent())	// double check
					secondTsCellStore.get().put(tc);	// slow path
				else {
					ITimestampedCellStore cellStore = this.create();	// create an {@link ITimestampedCellStore}
					cellStore.put(tc);
					put(row, col, cellStore);
				}
			} finally {
				writeLock.unlock();
			}
		}

        LOGGER.debug("End: put [row: {}, col: {}, tc: {}] into table.", row, col, tc);
	}
	
	@NotNull
    public abstract ITimestampedCellStore create();
	
	/**
	 * Put data (row, column, timestamped-cell-store).
	 * 
	 * @param row {@link Row} key
	 * @param col {@link Column} key
	 * @param tsCellStore {@link ITimestampedCellStore}; it cannot be {@code null}.
	 */
	protected void put(@NotNull Row row, @NotNull Column col, @NotNull ITimestampedCellStore tsCellStore) {
		writeLock.lock();
		try {
			table.put(row, col, tsCellStore);
		} finally {
			writeLock.unlock();
		}
	}

}
