package read;

import cores.SSDConstraint;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class SsdFileReader {
    public String[] readFile() throws FileNotFoundException {
        String[] result = new String[SSDConstraint.MAX_BOUNDARY];

        Scanner scanner = new Scanner(new File(SSDConstraint.FILENAME));


        return result;
    }
}