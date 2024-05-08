package read;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import static cores.SSDConstraint.*;
import static java.lang.Integer.parseInt;


public class SsdFileReader {
    public String[] readFile() {
        String[] result = new String[MAX_BOUNDARY];

        try {
            BufferedReader reader = setNandFileReader();

            String line = reader.readLine();
            while (fileNotEnd(line)) {
                result[getLba(line)] = getValue(line);
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException ignored) {
        }

        return result;
    }

    private static BufferedReader setNandFileReader() throws FileNotFoundException {
        return new BufferedReader(new FileReader
                (NAND_FILENAME));
    }

    private static boolean fileNotEnd(String line) {
        return line != null;
    }

    private static String getValue(String line) {
        if (line.split(" ").length == 2) {
            return line.split(" ")[1];
        }
        return null;
    }

    private static int getLba(String line) {
        return parseInt(line.split(" ")[0]);
    }

}