package benchmarking.logs;

import com.google.common.base.Charsets;
import com.google.common.base.MoreObjects;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.io.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static benchmarking.workload.WorkloadUtil.WorkloadParams.MPL;
import static java.lang.String.valueOf;
import static java.util.stream.Collectors.joining;
import static util.LogUtil.extractAborts;
import static util.LogUtil.extractParamVal;
import static util.LogUtil.extractRvsi;

/**
 * @author hengxin
 * @date 16-9-19
 */
public class Log2Dat {
    private static final Logger LOGGER = LoggerFactory.getLogger(Log2Dat.class);
    private static final String DAT_EXTENSION = ".dat";

    private final String logFile;
    private final Table<Integer, String, Double> logTable = HashBasedTable.create();

//    private final Multimap<Integer, AbortRate> table = ArrayListMultimap.create();

    public Log2Dat(String logFile) { this.logFile = logFile; }

    /**
     * Save log as a .dat file
     * - sharing the file name of {@link #logFile}
     * - with ".dat" file extension
     * - stored in the same directory with {@link #logFile}
     */
    public void saveAsDat() {
        fillTable(logFile);

        String datFile = new File(logFile).getParent() + File.separator
                + Files.getNameWithoutExtension(logFile) + DAT_EXTENSION;

        File file = new File(datFile);
        if (file.exists())
            try {
                Files.write("", file, Charsets.UTF_8);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

        saveColTitle(datFile);
        saveMplRows(datFile);
    }

    private void saveColTitle(String datFile) {
        String colTitle = logTable.columnKeySet().stream()
                .collect(joining("\t", "\t", System.lineSeparator()));

        try {
            Files.append(colTitle, new File(datFile), Charsets.UTF_8);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void saveMplRows(String datFile) {
        String rows = logTable.rowKeySet().stream()
                .map(this::getRow)
                .collect(Collectors.joining());

        try {
            Files.append(rows, new File(datFile), Charsets.UTF_8);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private String getRow(int mpl) {
        Map<String, Double> rowMap = logTable.row(mpl);
        return logTable.columnKeySet().stream()
                .map(rowMap::get)
                .map(String::valueOf)
                .collect(joining("\t", valueOf(mpl) + "\t", System.lineSeparator()));
    }

    private void fillTable(String logFile) {
        try {
            File log = new File(logFile);
            Files.readLines(log, Charsets.UTF_8)
                    .forEach(this::fillTableRow);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        // fillTable Double.NaN for empty values
        logTable.rowKeySet().forEach(row ->
                logTable.columnKeySet().forEach(col -> {
                    if (! logTable.contains(row, col))
                        logTable.put(row, col, Double.NaN);
                }));
    }

    private void fillTableRow(String line) {
        int mpl = Integer.parseInt(extractParamVal(line, MPL));
        String rvsi = extractRvsi(line);
        String aborts = extractAborts(line);

        AbortRate abortRate = new AbortRate(rvsi, aborts);
        put(mpl, abortRate);
    }

    private void put(int mpl, AbortRate abortRate) {
        logTable.put(mpl, "all_" + abortRate.rvsi, abortRate.all);
        logTable.put(mpl, "vc_" + abortRate.rvsi, abortRate.vc);
        logTable.put(mpl, "wcf_" + abortRate.rvsi, abortRate.wcf);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .addValue(logTable)
                .toString();
    }

    private class AbortRate {
        private String rvsi;

        private double all;
        private double vc;
        private double wcf;

        AbortRate(String rvsi, String line) {
            this.rvsi = rvsi;

            String[] parts = line.split(",");
            Arrays.stream(parts)
                    .forEach(part -> {
                        String[] kvs = part.split("=");
                        switch (kvs[0].trim()) {
                            case "#A/#T":
                                all = Double.parseDouble(kvs[1]);
                                break;
                            case "#!VC/#T":
                                vc = Double.parseDouble(kvs[1]);
                                break;
                            case "#!WCF/#T":
                                wcf = Double.parseDouble(kvs[1]);
                                break;
                            default:
                        }
                    });
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .addValue(rvsi)
                    .addValue(all)
                    .addValue(vc)
                    .addValue(wcf)
                    .toString();
        }

    }

}
