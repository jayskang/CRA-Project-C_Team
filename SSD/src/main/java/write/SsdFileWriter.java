package write;

import read.SsdFileReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static cores.SSDConstraint.*;

public class SsdFileWriter {

    private final SsdFileReader reader;

    public SsdFileWriter() {
        this.reader = new SsdFileReader();
        saveData(null);
    }

    public void store(int address, String value) {
        String[] nand = this.reader.readFile();
        nand[address] = value;
        saveData(nand);
    }

    private File checkFileExist() {
        File file = new File(FILE_ABSOLUTE_LOCATION + NAND_FILENAME);

        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException ignored) {
            }
        }
        return file;
    }

    private void saveData(String[] nand) {
        File file = checkFileExist();

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            StringBuilder stringBuilder;

            for (int i = 0; i < MAX_BOUNDARY; i += 1) {
                stringBuilder = new StringBuilder();

                stringBuilder.append(i);
                stringBuilder.append(" ");

                if (nand != null) {
                    stringBuilder.append(nand[i] == null ? DEFAULT_VALUE : nand[i]);
                } else {
                    stringBuilder.append(DEFAULT_VALUE);
                }
                stringBuilder.append("\n");

                bufferedWriter.write(stringBuilder.toString());
            }
            bufferedWriter.close();
        } catch (IOException ignored) {
        }
    }
}
