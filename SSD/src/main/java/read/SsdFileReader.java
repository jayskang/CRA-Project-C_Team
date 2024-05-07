package read;

import java.io.*;

import static cores.SSDConstraint.*;
import static java.lang.Integer.*;


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
        } catch (IOException e) {
        }

        return result;
    }
}