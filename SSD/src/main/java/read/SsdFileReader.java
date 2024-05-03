package read;

import cores.AddressConstraint;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class SsdFileReader {
    public String[] readFile() throws FileNotFoundException {
        String[] result = new String[AddressConstraint.MAX_BOUNDARY];

        Scanner scanner = new Scanner(new File("nand.txt"));

        return result;
    }
}