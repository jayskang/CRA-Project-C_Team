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
            fileWriter = new FileWriter(new File(RESULT_FILENAME), false);
            fileWriter.write(setResult(lba));
            fileWriter.close();
        } catch (IOException e) {
        }

    }

    private static String setResult(int lba) throws IOException {
        String[] result = SsdFileReader.readFile();
        if (isValueExists(result[lba])) {
            return result[lba];
        }
        return DEFAULT_VALUE;
    }

    private static boolean isValueExists(String result) {
        return result != null;
    }

    public boolean isValidAddress(int lba) {
        return lba >= MAX_BOUNDARY || lba < MIN_BOUNDARY;
    }
}