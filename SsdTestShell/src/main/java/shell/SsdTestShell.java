package shell;

import java.io.IOException;
import java.util.ArrayList;

import static constants.Command.*;
import static constants.Messages.ERROR_MSG_INVALID_COMMAND;

public class SsdTestShell implements ISsdCommand {
    public static final int MIN_ERASE_SIZE = 1;
    public static final int MAX_SSD_ERASE_SIZE = 10;
    private SSDExecutor ssd;

    public void setSsd(SSDExecutor ssd) {
        this.ssd = ssd;
    }

    @Override
    public void write(String lba, String data) throws IllegalArgumentException, IOException {
        checkLbaValidRange(lba);
        checkIsDataValid(data);
        ssd.write(lba, data);
    }

    @Override
    public String read(String lba) throws IllegalArgumentException, IOException {
        checkLbaValidRange(lba);
        return ssd.read(lba);
    }

    @Override
    public void fullwrite(String data) throws IllegalArgumentException, IOException {
        checkIsDataValid(data);
        for (int i = MIN_LBA; i <= MAX_LBA; i++) {
            ssd.write(Integer.toString(i), data);
        }
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

        while(isPossibleToSeperate(lbaNum, sizeNum)){
            ssd.erase(String.valueOf(lbaNum), String.valueOf(MAX_SSD_ERASE_SIZE));
            lbaNum += MAX_SSD_ERASE_SIZE;
            sizeNum -= MAX_SSD_ERASE_SIZE;
        }

        if (isRemainEraseLba(sizeNum, lbaNum)) {
            eraseRemainLbaArea(lbaNum, sizeNum);
        }
    }

    private void eraseRemainLbaArea(int lbaNum, int sizeNum) throws IOException {
        int erasableArea = MAX_LBA - lbaNum + 1;
        if (erasableArea < sizeNum) {
            ssd.erase(String.valueOf(lbaNum), String.valueOf(erasableArea));
        } else {
            ssd.erase(String.valueOf(lbaNum), String.valueOf(sizeNum));
        }
    }

    @Override
    public void eraserange(String startLba, String endLba) throws IllegalArgumentException, IOException {
        checkStartEndLbaValue(startLba, endLba);
        erase(startLba, String.valueOf(Integer.parseInt(endLba) - Integer.parseInt(startLba)));
    }

    private void checkIsDataValid(String data) throws IllegalArgumentException {
        if (data == null || !data.matches(VALUE_FORMAT_REGEX))
            throw new IllegalArgumentException(ERROR_MSG_INVALID_COMMAND);
    }

    private void checkLbaValidRange(String lba) throws IllegalArgumentException{
        try {
            if(isLbaOutOfRange(lba))
                throw new IllegalArgumentException(ERROR_MSG_INVALID_COMMAND);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ERROR_MSG_INVALID_COMMAND);
        }
    }

    private void checkEraseEndLbaValidRange(String endLba) throws IllegalArgumentException{
        try {
            if(isEraseEndLbaOutOfRange(endLba))
                throw new IllegalArgumentException(ERROR_MSG_INVALID_COMMAND);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ERROR_MSG_INVALID_COMMAND);
        }
    }

    private void checkEraseStartEndLbaValidRange(String startLba, String endLba) {
        if (Integer.parseInt(startLba) >= Integer.parseInt(endLba))
            throw new IllegalArgumentException(ERROR_MSG_INVALID_COMMAND);
    }

    private void checkStartEndLbaValue(String startLba, String endLba) {
        checkLbaValidRange(startLba);
        checkEraseEndLbaValidRange(endLba);
        checkEraseStartEndLbaValidRange(startLba, endLba);
    }

    private void checkEraseValid(String lba, String size) {
        checkLbaValidRange(lba);
        checkEraseValidSize(size);
    }

    private boolean isLbaOutOfRange(String lba) {
        int lbaNum = Integer.parseInt(lba);
        return lbaNum > MAX_LBA || lbaNum < MIN_LBA;
    }

    private boolean isEraseEndLbaOutOfRange(String endLba) {
        int endLbaNum = Integer.parseInt(endLba);
        return endLbaNum > MAX_LBA + 1 || endLbaNum < MIN_LBA + 1;
    }

    private static boolean isPossibleToSeperate(int lbaNum, int sizeNum) {
        int erasableArea = MAX_LBA - lbaNum + 1;
        return sizeNum >= MAX_SSD_ERASE_SIZE && erasableArea >= MAX_SSD_ERASE_SIZE;
    }

    private static boolean isRemainEraseLba(int sizeNum, int lbaNum) {
        return sizeNum >= MIN_ERASE_SIZE && lbaNum <= MAX_LBA;
    }

    private void checkEraseValidSize(String size) throws IllegalArgumentException {
        try {
            int eraseSizeNum = Integer.parseInt(size);
            if(eraseSizeNum < MIN_ERASE_SIZE)
                throw new IllegalArgumentException(ERROR_MSG_INVALID_COMMAND);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ERROR_MSG_INVALID_COMMAND);
        }
    }
}