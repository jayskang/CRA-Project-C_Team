import java.io.IOException;
import java.util.ArrayList;

import static constants.Command.*;
import static constants.Messages.ERROR_MSG_INVALID_COMMAND;

public class SsdTestShell implements ISsdCommand{
    public static final int MIN_ERASE_SIZE = 1;
    private SSDExecutor ssd;

    public void setSsd(SSDExecutor ssd) {
        this.ssd = ssd;
    }

    @Override
    public void write(String lba, String data) throws IllegalArgumentException, IOException {
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
    public void fullwrite(String data) throws IllegalArgumentException, IOException {
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

    @Override
    public void erase(String lba, String size) throws IllegalArgumentException, IOException {
        checkIsEraseSizeValid(size);
        ssd.erase(lba, size);
        //Shell 명령어 1 : erase [LBA] [SIZE]
        //• SSD에 명령어를 수행한다.
        //SSD 명령어 : E [LBA] [SIZE]
        //• 특정 LBA 부터 특정 Size 까지 내용을 삭제한다.
        //• 삭제할 수 있는 최대 Size는 최대 10칸이다.
        //• SSD에서 삭제하면 0x00000000이 된다
    }

    private void checkIsEraseSizeValid(String size) throws IllegalArgumentException {
        try {
            int eraseSizeNum = Integer.parseInt(size);
            if(isSizeOutOfRange(eraseSizeNum))
                throw new IllegalArgumentException(ERROR_MSG_INVALID_COMMAND);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ERROR_MSG_INVALID_COMMAND);
        }
    }

    private boolean isSizeOutOfRange(int eraseSizeNum) {
        return eraseSizeNum < MIN_ERASE_SIZE;
    }

    @Override
    public void eraserange(String startLba, String endLba) throws IllegalArgumentException, IOException {
        //erase_range [Start LBA] [End LBA]
        //• Start LBA 부터 END LBA 직전 까지 내용을 삭제한다.
    }


}
