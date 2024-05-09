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
    }

    public void store(int address, String value) {
        String[] nand = this.reader.readFile();
        nand[address] = value;
        saveData(nand);
    }

    private void saveData(String[] nand) {
        File file = checkFileExist();

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));

            for (int lba = 0; lba < MAX_BOUNDARY; lba += 1) {

                bufferedWriter.write(getStringForWrite(nand, lba));
            }

            bufferedWriter.close();
        } catch (IOException ignored) {
        }
    }

    private File checkFileExist() {
        File file = new File(NAND_FILENAME);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ignored) {
            }
        }
        return file;
    }

    private static String getStringForWrite(String[] nand, int lba) {
        String result = "";
        StringBuilder tempString = new StringBuilder();

        tempString.append(lba);
        tempString.append(" ");

        if (nand != null) {
            tempString.append(getValue(nand, lba));
        } else {
            tempString.append(DEFAULT_VALUE);
        }
        tempString.append("\n");

        result = tempString.toString();
        return result;
    }

    private static String getValue(String[] nand, int lba) {
        return nand[lba] == null ? DEFAULT_VALUE : nand[lba];
    }
}
