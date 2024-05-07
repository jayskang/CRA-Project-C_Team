package read;

import cores.SSDConstraint;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static cores.SSDConstraint.*;


public class ReadModule implements ReadCore {
    private static File file;
    private static FileWriter fileWriter;

    public void read(int lba) {

        if (isValidAddress(lba)) {
            return;
        }

        file = new File(RESULT_FILENAME);
        try {
            fileWriter = new FileWriter(file);
            String[] result = SsdFileReader.readFile();
            if(!result[lba].equals(null)){
                fileWriter.write(result[lba]);
            }
            fileWriter.close();
        } catch (IOException e) {
        }




    }

    public  boolean isValidAddress(int lba) {
        return lba >= MAX_BOUNDARY || lba < MIN_BOUNDARY;
    }
}