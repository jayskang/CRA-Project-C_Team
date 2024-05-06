package read;

import cores.SSDConstraint;

import java.io.*;


public class SsdFileReader {
    public String[] readFile() throws IOException {
        String[] result = new String[SSDConstraint.MAX_BOUNDARY];


        BufferedReader reader = new BufferedReader(new FileReader(SSDConstraint.FILENAME));
        String str;
        while ((str = reader.readLine()) != null) {

        }
        reader.close();



        return result;
    }
}