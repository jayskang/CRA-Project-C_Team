import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static constants.Command.*;
import static constants.Messages.*;

public class SSD {

    SSDResultFileReader resultFileReader;

    void setResultFileReader(SSDResultFileReader reader){
        this.resultFileReader = reader;
    }

    public void write(String lbs, String data) {}

    public String read(String lba) throws IOException{
        execSsdReadCommand(lba);
        return readResultFile();
    }

    public void execSsdReadCommand(String lba) throws IOException {
        try {
            Process process = new ProcessBuilder(
                    SSD_EXEC_JAVA_COMMAND, SSD_EXEC_JAR_OPTION, SSD_EXEC_JAR_FILE_PATH
                    , SSD_EXEC_READ_OPTION, lba).start();
        } catch(Exception e){
            throw new IOException(ERROR_MSG_SSD_CANNOT_EXEC);
        }
    }

    public String readResultFile() throws IOException {
        try {
            return resultFileReader.readFile();
        } catch (IOException e) {
            throw new IOException(ERROR_MSG_RESULT_FILE_NOT_FOUNDED);
        }
    }
}
