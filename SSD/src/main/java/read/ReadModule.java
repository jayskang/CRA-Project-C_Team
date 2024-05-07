package read;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static cores.SSDConstraint.*;


public class ReadModule implements ReadCore {
    private static FileWriter fileWriter;

    public void read(int lba) {

        if (isValidAddress(lba)) {
            return;
        }

        try {
            fileWriter = new FileWriter(new File(FILE_ABSOLUTE_LOCATION + RESULT_FILENAME), false);
            fileWriter.write(getResult(lba));
            fileWriter.close();
        } catch (IOException e) {
        }

    }

    public boolean isValidAddress(int lba) {
        return lba >= MAX_BOUNDARY || lba < MIN_BOUNDARY;
    }

    private static String getResult(int lba) throws IOException {
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