package write;

import read.SsdFileReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static cores.SSDConstraint.*;

public class SsdFileWriter {

    private final SsdFileReader reader;
    private BufferedWriter bufferedWriter;

    public SsdFileWriter() {
        this.reader = new SsdFileReader();
        saveData();
    }

    public void store(int address, String value) {
        try {
            String[] nand = this.reader.readFile();
            File file = checkFileExist();

            nand[address] = value;

            this.bufferedWriter = new BufferedWriter(new FileWriter(file));
            StringBuilder stringBuilder;

            for(int i = 0; i < MAX_BOUNDARY; i += 1) {

                stringBuilder = new StringBuilder();

                stringBuilder.append(i);
                stringBuilder.append(" ");
                stringBuilder.append("0x00000000");
                stringBuilder.append("\n");

                this.bufferedWriter.write(stringBuilder.toString());
            }
            this.bufferedWriter.close();
        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
        }
    }

    private File checkFileExist() {
        File file = new File(NAND_ABSOLUTE_LOCATION + FILENAME);

        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
            }
        }
        return file;
    }

    private void saveData() {
        File file = checkFileExist();

        try {
            this.bufferedWriter = new BufferedWriter(new FileWriter(file));
            StringBuilder stringBuilder;

            for (int i = 0; i < MAX_BOUNDARY; i += 1) {
                stringBuilder = new StringBuilder();

                stringBuilder.append(i);
                stringBuilder.append(" ");
                stringBuilder.append(INITIAL_STATE);
                stringBuilder.append("\n");

                this.bufferedWriter.write(stringBuilder.toString());
            }
            this.bufferedWriter.close();
        } catch (IOException ioException) {
            return;
        }
    }
}
