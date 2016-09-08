package client.workload.operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hengxin
 * @date 16-9-7
 */
public class WriteOperation extends Operation {
    private static final Logger LOGGER = LoggerFactory.getLogger(WriteOperation.class);

    WriteOperation(String row, String col, String val) {
        super(row, col, val);
    }

}
