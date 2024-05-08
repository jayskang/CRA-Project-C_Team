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

    private void checkIsEraseRangeEndLbaValid(String lba) throws IllegalArgumentException{
        try {
            int lbaNum = Integer.parseInt(lba);
            if(isEraseEndLbaOutOfRange(lbaNum))
                throw new IllegalArgumentException(ERROR_MSG_INVALID_COMMAND);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ERROR_MSG_INVALID_COMMAND);
        }
    }

    private boolean isLbaOutOfRange(int lba) {
        return lba > MAX_LBA || lba < MIN_LBA;
    }

    private boolean isEraseEndLbaOutOfRange(int endLba) {
        return endLba > MAX_LBA + 1 || endLba < MIN_LBA + 1;
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
    public void eraserange(String startLba, String endLba) throws IllegalArgumentException, IOException {
        checkIsLbaValid(startLba);
        checkIsEraseRangeEndLbaValid(endLba);
        compareStartEndLbaValue(startLba, endLba);

        int startLbaNum = Integer.parseInt(startLba);
        int endLbaNum = Integer.parseInt(endLba);

        erase(startLba, String.valueOf(endLbaNum - startLbaNum));
    }

    private void compareStartEndLbaValue(String startLba, String endLba) {
        if (Integer.parseInt(startLba) >= Integer.parseInt(endLba))
            throw new IllegalArgumentException(ERROR_MSG_INVALID_COMMAND);
    }

    @Override
    public void erase(String lba, String size) throws IllegalArgumentException, IOException {
        checkEraseValid(lba, size);
        int lbaNum = Integer.parseInt(lba);
        int sizeNum = Integer.parseInt(size);
        int erasableArea = MAX_LBA - lbaNum + 1;

        while(isPossibleToSeperate(sizeNum, erasableArea)){
            ssd.erase(String.valueOf(lbaNum), String.valueOf(MAX_SSD_ERASE_SIZE));
            lbaNum += MAX_SSD_ERASE_SIZE;
            sizeNum -= MAX_SSD_ERASE_SIZE;
        }

        if (isRemainEraseLba(sizeNum, lbaNum)) {
            eraseRemainLbaArea(lbaNum, sizeNum);
        }
    }

    private void eraseRemainLbaArea(int lbaNum, int sizeNum) throws IOException {
        int erasableArea;
        erasableArea = MAX_LBA - lbaNum + 1;
        if (erasableArea < sizeNum) {
            ssd.erase(String.valueOf(lbaNum), String.valueOf(erasableArea));
        } else {
            ssd.erase(String.valueOf(lbaNum), String.valueOf(sizeNum));
        }
    }

    private static boolean isPossibleToSeperate(int sizeNum, int erasableArea) {
        return sizeNum >= MAX_SSD_ERASE_SIZE && erasableArea >= MAX_SSD_ERASE_SIZE;
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
}