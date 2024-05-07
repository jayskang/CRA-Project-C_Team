import java.io.IOException;
import java.util.ArrayList;

import static constants.Command.*;
import static constants.Messages.ERROR_MSG_INVALID_COMMAND;

public class SsdTestShell implements ISsdCommand{



    private SSD ssd;

    public void setSsd(SSD ssd) {
        this.ssd = ssd;
    }

    @Override
    public void write(String lba, String data) {
        checkIsLbaValid(lba);
        checkIsDataValid(data);
        ssd.write(lba, data);
    }

    @Override
    public String read(String lba) throws IllegalArgumentException, IOException {
        checkIsLbaValid(lba);
        return ssd.read(lba);
    }

    @Override
    public void fullwrite(String data) {
        checkIsDataValid(data);
        for (int i = MIN_LBA; i <= MAX_LBA; i++) {
            ssd.write(Integer.toString(i), data);
        }
    }

    private void checkIsDataValid(String data) throws IllegalArgumentException {
        if (data == null || !data.matches(VALUE_FORMAT_REGEX))
            throw new IllegalArgumentException(ERROR_MSG_INVALID_COMMAND);
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
    public ArrayList<String> fullread() throws IllegalArgumentException, IOException {
        ArrayList<String> list = new ArrayList<>();
        for(int lba = MIN_LBA; lba <= MAX_LBA; lba++){
            list.add(ssd.read(String.valueOf(lba)));
        }
        return list;
    }
}
