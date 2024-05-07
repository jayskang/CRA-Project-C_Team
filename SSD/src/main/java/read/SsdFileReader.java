package read;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static cores.SSDConstraint.*;
import static java.lang.Integer.parseInt;


public class SsdFileReader {
    public String[] readFile() {
        String[] result = new String[MAX_BOUNDARY];

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(FILE_ABSOLUTE_LOCATION + NAND_FILENAME));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] lbaAndValue = line.split(" ");
                if (lbaAndValue.length == 2) {
                    result[parseInt(lbaAndValue[0])] = lbaAndValue[1];
                }
            }
            reader.close();
        } catch (IOException ignored) {
        }

        return result;
    }
}