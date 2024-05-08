package read;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static cores.SSDConstraint.*;


public class ReadModule implements ReadCore {
    private static FileWriter fileWriter;

    public void read(int lba) {

        if (isNotValidLba(lba)) {
            return;
        }

        try {
            fileWriter = new FileWriter(new File(RESULT_FILENAME), false);
            fileWriter.write(getResult(lba));
            fileWriter.close();
        } catch (IOException e) {
        }

    }

    public boolean isNotValidLba(int lba) {
        return lba >= MAX_BOUNDARY || lba < MIN_BOUNDARY;
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