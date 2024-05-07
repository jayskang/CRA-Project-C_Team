import java.io.IOException;

public class SSD {
    public static final String ERROR_MSG_RESULT_FILE_NOT_FOUNDED = "Result File Not Founded";
    SSDResultFileReader resultFileReader;

    void setResultFileReader(SSDResultFileReader reader){
        this.resultFileReader = reader;
    }

    public void write(String lbs, String data) {}

    public String read(String lba) {
        execSsdReadCommand(lba);
        return readResultFile();
    }

    public void execSsdReadCommand(String lba) {
        // 1. ssd java 프로그램을 실행시켜야 함.
        //    ※ 명령문 : ssd R [LBA]
    }

    public String readResultFile() {
        try {
            return resultFileReader.readFile();
        } catch (IOException e) {
            return ERROR_MSG_RESULT_FILE_NOT_FOUNDED;
        }
    }
}
