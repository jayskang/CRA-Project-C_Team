import java.io.IOException;

import static constants.Command.*;
import static constants.Messages.*;

public class SSDExecutor {
    SSDResultFileReader resultFileReader;

    void setResultFileReader(SSDResultFileReader reader){
        this.resultFileReader = reader;
    }

    public void write(String lba, String data) throws IOException {
        execSsdWriteCommand(lba, data);
    }

    public void execSsdWriteCommand(String lba, String data) throws IOException {
        try {
            Process process = new ProcessBuilder(
                    SSD_EXEC_JAVA_COMMAND, SSD_EXEC_JAR_OPTION, SSD_EXEC_JAR_FILE_PATH
                    , SSD_WRITE_OPTION_CMD, lba, data).start();
        } catch(Exception e){
            throw new IOException(ERROR_MSG_SSD_CANNOT_EXEC);
        }
    }

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
            return "0x00000000";
        }
    }
}
