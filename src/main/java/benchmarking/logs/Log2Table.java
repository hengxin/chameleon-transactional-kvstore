package benchmarking.logs;

import com.google.common.base.Charsets;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.io.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hengxin
 * @date 16-9-19
 */
public class Log2Table {
    private static final Logger LOGGER = LoggerFactory.getLogger(Log2Table.class);
    private final Multimap<Integer, AbortRate> table = ArrayListMultimap.create();

    public void transform(String file) {
        try {
            List<String> lines = Files.readLines(new File(file), Charsets.UTF_8);
            lines.stream()
                    .filter(this::containsData)
                    .forEach(this::fillTable);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void saveAsDat(String file) {
        String dat = table.keySet().stream()
                .map(this::mplStr)
                .collect(Collectors.joining(System.lineSeparator()));
        try {
            Files.write(dat, new File(file), Charsets.UTF_8);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private String mplStr(int mpl) {
        return table.get(mpl).stream()
                .map(AbortRate::tabStr)
                .collect(Collectors.joining("\t", String.valueOf(mpl) + "\t", ""));
    }

    private void fillTable(String line) {
        int mpl = extractMpl(line);
        String rvsi = extractRvsi(line);
        String aborts = extractAborts(line);

        AbortRate abortRate = new AbortRate(rvsi, aborts);
        table.put(mpl, abortRate);
    }

    private int extractMpl(String line) {
        return Integer.parseInt(extract(line, "mpl=", ','));
    }

    private String extractRvsi(String line) {
        String rvsi = extract(line, "RVSITriple{", '}');
        return Arrays.stream(rvsi.split(","))
                .map(String::trim)
                .map(kv -> kv.split("=")[1])
                .collect(Collectors.joining(""));
    }

    private String extractAborts(String line) {
        return extract(line, "WorkloadStatistics{", '}');
    }

    private String extract(String line, String key, char sep) {
        int keyBeginIndex = line.indexOf(key);
        int keyEndIndex = keyBeginIndex + key.length();
        int sepIndex = line.indexOf(sep, keyEndIndex);
        return line.substring(keyEndIndex, sepIndex);
    }


    private boolean containsData(String line) {
        return line.startsWith("BatchRunner");
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .addValue(table)
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

        String tabStr() {
            return String.valueOf(all) + '\t' + vc + '\t' + wcf;
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

    public static void main(String[] args) {
        Log2Table log2Table = new Log2Table();

        log2Table.transform("logs/log2table-rw4.log");
        LOGGER.info("[table: {}].", log2Table);

        log2Table.saveAsDat("logs/log2table-rw4.dat");

    }
}
