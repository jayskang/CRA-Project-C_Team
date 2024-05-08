package read;

import cores.SSDCommonUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static cores.SSDConstraint.*;


public class ReadModule extends SSDCommonUtils implements ReadCore {
    private static FileWriter fileWriter;

    public ReadModule() {
        super();
    }

    public void read(int lba) {
        if (this.checkLbaBoundary(lba)) {
            try {
                fileWriter = new FileWriter(new File(RESULT_FILENAME), false);
                fileWriter.write(getResult(lba));
                fileWriter.close();
            } catch (IOException ignored) {
            }
        }
    }

    public String getResult(int lba) throws IOException {
        SsdFileReader ssdFileReader = new SsdFileReader();
        String[] result = ssdFileReader.readFile();
        if (isValueExists(result[lba])) {
            return result[lba];
        }
        return DEFAULT_VALUE;
    }

    private static boolean isValueExists(String result) {
        return result != null;
    }
}