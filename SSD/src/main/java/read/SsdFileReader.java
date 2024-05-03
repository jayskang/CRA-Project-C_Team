package read;

import cores.SSDConstraint;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class SsdFileReader {
    public String[] readFile() throws FileNotFoundException {
        String[] result = new String[SSDConstraint.MAX_BOUNDARY];

        Scanner scanner = new Scanner(new File(SSDConstraint.FILENAME));
        while(scanner.hasNext()){
            int address = parseInt(scanner.next());
            String value = scanner.next();
            result[address] = value;
        }

        return result;
    }
}
