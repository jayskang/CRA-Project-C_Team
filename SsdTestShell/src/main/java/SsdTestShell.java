import java.io.IOException;
import java.util.ArrayList;

public class SsdTestShell implements ISsdCommand{
    public static final int MAX_LBA = 99;
    public static final int MIN_LBA = 0;
    public static final String ERROR_MSG_INVALID_COMMAND = "INVALID COMMAND";
    private SSD ssd;

    public void setSsd(SSD ssd) {
        this.ssd = ssd;
    }

    @Override
    public void write(String lba, String data) {
        try {
            checkIsLbaValid(lba);
            checkIsDataValid(data);
            ssd.write(lba, data);
        } catch(IllegalArgumentException e) {
//            printError(e);
        }
    }

    @Override
    public String read(String lba) throws IllegalArgumentException, IOException {
        try{
            int param = Integer.parseInt(lba);
            if(isLbaOutOfRange(param))
                throw new NumberFormatException(ERROR_MSG_INVALID_COMMAND);
            return ssd.read(lba);
        } catch (NumberFormatException ne){
            throw new IllegalArgumentException(ERROR_MSG_INVALID_COMMAND);
        }
    }

    @Override
    public void fullwrite(String data) {

    }
    private void checkIsDataValid(String data) throws IllegalArgumentException {
        if (isInvalidDataFormat(data)) {
            throw new IllegalArgumentException("INVALID Argument. 2번째 인자가 유효하지 않습니다.");
        }
        try {
            Integer.parseInt(data.substring(2), 16);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("INVALID Argument. 2번째 인자가 유효하지 않습니다.");
        }
    }

    private boolean isInvalidDataFormat(String data) {
        return data == null || !data.startsWith("0x") || data.length() != 10;
    }

    private void checkIsLbaValid(String lba) throws IllegalArgumentException{
        try {
            int lbaNum = Integer.parseInt(lba);
            if(isLbaOutOfRange(lbaNum))
                throw new IllegalArgumentException("Invalid Argument. 1 번째 인자가 유효하지 않습니다.");
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid Argument. 1 번째 인자가 유효하지 않습니다.");
        }
    }

    private boolean isLbaOutOfRange(int param) {
        return param > MAX_LBA || param < MIN_LBA;
    }

    @Override
    public ArrayList<String> fullread() throws IllegalArgumentException {
        return null;
    }


}
