package write;

import cores.SSDCommonUtils;
import read.SsdFileReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static cores.SSDConstraint.*;

public class SsdFileWriter extends SSDCommonUtils {

    private final SsdFileReader reader;

    public SsdFileWriter() {
        super();
        this.reader = new SsdFileReader();
        saveData(null);
    }

    public void store(int address, String value) {
        try {
            if (this.checkAddressBoundary(address)) {
                String[] nand = this.reader.readFile();
                nand[address] = value;
                saveData(nand);
            }
        } catch (IOException ignored) {
        }
    }

    private File checkFileExist() {
        File file = new File(NAND_ABSOLUTE_LOCATION + FILENAME);

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
                    stringBuilder.append(nand[i]);
                } else {
                    stringBuilder.append(INITIAL_STATE);
                }
                stringBuilder.append("\n");

                bufferedWriter.write(stringBuilder.toString());
            }
            bufferedWriter.close();
        } catch (IOException ignored) {
        }
    }
}
