package shell;

import java.io.IOException;

import static constants.Command.*;
import static constants.Messages.*;
import static java.lang.Thread.sleep;

public class SSDExecutor {
    SSDResultFileReader resultFileReader;
    String ssdProgramPath;

    public SSDExecutor(){
        this.ssdProgramPath = SSD_EXEC_JAR_FILE_PATH;
    }
    public void setSsdProgramPath(String ssdProgramPath) {
        this.ssdProgramPath = ssdProgramPath;
    }
    public void setResultFileReader(SSDResultFileReader reader){
        this.resultFileReader = reader;
    }

    public void write(String lba, String data) throws IOException {
        execSsdWriteCommand(lba, data);
    }

    public void execSsdWriteCommand(String lba, String data) throws IOException {
        try {
            Process process = new ProcessBuilder(
                    SSD_EXEC_JAVA_COMMAND, SSD_EXEC_JAR_OPTION, ssdProgramPath
                    , SSD_WRITE_OPTION_CMD, lba, data)
                    .start();
            process.waitFor();
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
                    SSD_EXEC_JAVA_COMMAND, SSD_EXEC_JAR_OPTION, ssdProgramPath
                    , SSD_READ_OPTION_CMD, lba)
                    .start();
            process.waitFor();
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

    public void erase(String lba, String size) throws IOException {
        try {
            Process process = new ProcessBuilder(
                    SSD_EXEC_JAVA_COMMAND, SSD_EXEC_JAR_OPTION, ssdProgramPath
                    , SSD_ERASE_OPTION_CMD, lba, size)
                    .start();
            process.waitFor();
        } catch(Exception e){
            throw new IOException(ERROR_MSG_SSD_CANNOT_EXEC);
        }
    }

    public void flush() throws IOException {
        try {
            Process process = new ProcessBuilder(
                    SSD_EXEC_JAVA_COMMAND, SSD_EXEC_JAR_OPTION, ssdProgramPath
                    , SSD_FLUSH_OPTION_CMD)
                    .start();
            process.waitFor();
        } catch(Exception e){
            throw new IOException(ERROR_MSG_SSD_CANNOT_EXEC);
        }
    }
}
