import java.io.IOException;

import static constants.Command.*;
import static constants.Messages.*;

public class SSDExecutor {
    SSDResultFileReader resultFileReader;
    String ssdProgramPath;

    public SSDExecutor(){
        this.ssdProgramPath = SSD_EXEC_JAR_FILE_PATH;
    }
    public void setSsdProgramPath(String ssdProgramPath) {
        this.ssdProgramPath = ssdProgramPath;
    }
    void setResultFileReader(SSDResultFileReader reader){
        this.resultFileReader = reader;
    }

    public void write(String lba, String data) throws IOException {
        execSsdWriteCommand(lba, data);
    }

    public void execSsdWriteCommand(String lba, String data) throws IOException {
        try {
            Thread.sleep(10);
            Process process = new ProcessBuilder(
                    SSD_EXEC_JAVA_COMMAND, SSD_EXEC_JAR_OPTION, ssdProgramPath
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
            Thread.sleep(10);
            new ProcessBuilder(
                    SSD_EXEC_JAVA_COMMAND, SSD_EXEC_JAR_OPTION, ssdProgramPath
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

    public void erase(String lba, String size) {

    }
}
