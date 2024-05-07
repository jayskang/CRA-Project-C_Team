package read;

import cores.SSDConstraint;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static cores.SSDConstraint.*;
import static java.lang.Integer.parseInt;


public class ReadModule implements ReadCore {
    private File file;
    private FileWriter fileWriter;

    public void read(int lba) {

        if (isValidAddress(lba)) {
            return;
        }

        file = new File(RESULT_FILENAME);
        try {
            fileWriter = new FileWriter(file);
        } catch (IOException e) {
            return;
        }


    }

    public boolean isValidAddress(int lba) {
        return lba >= MAX_BOUNDARY || lba < MIN_BOUNDARY;
    }
}