import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SSDResultFileReader {
    public static final String RESULT_FILE_PATH = "result.txt";

    public String readFile() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(RESULT_FILE_PATH));
        String result = reader.readLine();
        reader.close();
        return result;
    }
}
