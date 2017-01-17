package kvs.table;

import org.jetbrains.annotations.NotNull;

/**
 * @author hengxin
 * @date Created on 11-11-2015
 * 
 * <p> Tables in master sites which use {@link MultiTimestampedCellsStore}.
 */
public class MasterTable extends AbstractTable {
	@NotNull
    @Override
	public ITimestampedCellStore create() {
		ITimestampedCellStore cell_store = new MultiTimestampedCellsStore();
		cell_store.startGCDaemon();
		return cell_store;
	}
}
