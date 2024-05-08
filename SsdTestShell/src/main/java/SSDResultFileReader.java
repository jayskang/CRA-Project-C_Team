import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static constants.Command.RESULT_FILE_PATH;

public class SSDResultFileReader {
    private String resultFilePath;

    public void setResultFilePath(String resultFilePath) {
        this.resultFilePath = resultFilePath;
    }

    public SSDResultFileReader(){
        this.resultFilePath = RESULT_FILE_PATH;
    }
    public String readFile() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(resultFilePath));
        String result =reader.readLine();
        reader.close();
        return result;
    }
}
