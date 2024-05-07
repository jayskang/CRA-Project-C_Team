package read;

import cores.SSDConstraint;

import java.io.*;

import static java.lang.Integer.*;

//NAND 가 빈파일일때 cHeck
public class SsdFileReader {
    public String[] readFile() throws IOException {
        String[] result = new String[SSDConstraint.MAX_BOUNDARY];

        BufferedReader reader = new BufferedReader(new FileReader(SSDConstraint.FILENAME));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] addressAndValue = line.split(" ");
            if(addressAndValue.length==2) {
                result[parseInt(addressAndValue[0])] =addressAndValue[1];
            }
        }
        reader.close();

        return result;
    }
}