package read;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static cores.SSDConstraint.MAX_BOUNDARY;
import static cores.SSDConstraint.NAND_FILENAME;
import static java.lang.Integer.parseInt;


public class SsdFileReader {
    public String[] readFile() {
        String[] result = new String[MAX_BOUNDARY];

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(NAND_FILENAME));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] addressAndValue = line.split(" ");
                if (addressAndValue.length == 2) {
                    result[parseInt(addressAndValue[0])] = addressAndValue[1];
                }
            }
            reader.close();
        } catch (IOException ignored) {
        }

        return result;
    }
}