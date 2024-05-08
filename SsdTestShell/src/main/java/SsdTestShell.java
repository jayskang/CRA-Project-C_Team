import java.io.IOException;
import java.util.ArrayList;

import static constants.Command.*;
import static constants.Messages.ERROR_MSG_INVALID_COMMAND;

public class SsdTestShell implements ISsdCommand{
    public static final int MIN_ERASE_SIZE = 1;
    public static final int MAX_SSD_ERASE_SIZE = 10;
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
        checkEraseValid(lba, size);
        int lbaNum = Integer.parseInt(lba);
        int sizeNum = Integer.parseInt(size);

        int erasableArea = MAX_LBA - lbaNum + 1;

        // 10 이상 size 쪼개서 명령 날리기
        while(sizeNum >= MAX_SSD_ERASE_SIZE && erasableArea >= MAX_SSD_ERASE_SIZE){
            ssd.erase(String.valueOf(lbaNum), String.valueOf(MAX_SSD_ERASE_SIZE));
            lbaNum += MAX_SSD_ERASE_SIZE;
            sizeNum -= MAX_SSD_ERASE_SIZE;
        }

        if (!isRemainEraseLba(sizeNum, lbaNum)) return;
        // 잔여분 처리하기
        erasableArea = MAX_LBA - lbaNum + 1;
        if (erasableArea < sizeNum) {
            ssd.erase(String.valueOf(lbaNum), String.valueOf(erasableArea));
        } else { // 잔여 size만큼 모두 erase 가능하면
            ssd.erase(String.valueOf(lbaNum), String.valueOf(sizeNum));
        }
    }

    private void checkEraseValid(String lba, String size) {
        checkIsLbaValid(lba);
        checkIsEraseSizeValid(size);
    }

    private static boolean isRemainEraseLba(int sizeNum, int lbaNum) {
        return sizeNum >= MIN_ERASE_SIZE && lbaNum <= MAX_LBA;
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
