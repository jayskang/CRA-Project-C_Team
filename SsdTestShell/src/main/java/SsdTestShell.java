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
        checkIsLbaValid(lba);
        return ssd.read(lba);
    }

    @Override
    public void fullwrite(String data) {

    }
    private void checkIsDataValid(String data) throws IllegalArgumentException {
        if (isInvalidDataFormat(data)) {
            throw new IllegalArgumentException(ERROR_MSG_INVALID_COMMAND);
        }
        try {
            Integer.parseInt(data.substring(2), 16);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ERROR_MSG_INVALID_COMMAND);
        }
    }

    private boolean isInvalidDataFormat(String data) {
        return data == null || !data.startsWith("0x") || data.length() != 10;
    }

    private void checkIsLbaValid(String lba) throws IllegalArgumentException{
        try {
            int lbaNum = Integer.parseInt(lba);
            if(isLbaOutOfRange(lbaNum))
                throw new IllegalArgumentException(ERROR_MSG_INVALID_COMMAND);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ERROR_MSG_INVALID_COMMAND);
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
