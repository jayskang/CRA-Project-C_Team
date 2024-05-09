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

        getFileContents(result);

        return result;
    }

    private static void getFileContents(String[] result) {
        try {
            BufferedReader reader = setNandFileReader();
            String fileContents = reader.readLine();

            while (fileNotEnd(fileContents)) {
                result[getLba(fileContents)] = getValue(fileContents);
                fileContents = reader.readLine();
            }

            reader.close();
        } catch (IOException ignored) {
        }
    }

    private static BufferedReader setNandFileReader() throws FileNotFoundException {
        return new BufferedReader(new FileReader(NAND_FILENAME));
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