public class SsdTestShell implements ISsdCommand{
    public static final int MAX_LBA = 99;
    public static final int MIN_LBA = 0;
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
        }
        catch (Exception e) {
            printError(e);
        }
    }

    @Override
    public void read(String lba) {
        try{
            checkIsLbaValid(lba);
            ssd.read(lba);
            System.out.println("read success: " + lba);
        } catch (Exception e){
            printError(e);
        }
    }

    @Override
    public void fullwrite(String data) {
        try {
            checkIsDataValid(data);
            for (int i = MIN_LBA; i <= MAX_LBA; i++) {
                write(Integer.toString(i), data);
            }
        } catch (Exception e) {
            printError(e);
        }
    }

    private static void checkIsDataValid(String data) throws IllegalArgumentException {
        if (isInvalidDataFormat(data)) {
            throw new IllegalArgumentException("INVALID Argument. 2번째 인자가 유효하지 않습니다.");
        }
        try {
            Integer.parseInt(data.substring(2), 16);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("INVALID Argument. 2번째 인자가 유효하지 않습니다.");
        }
    }

    private static boolean isInvalidDataFormat(String data) {
        return !data.startsWith("0x") || data.length() != 10;
    }

    private static void checkIsLbaValid(String lba) throws IllegalArgumentException{
        try {
            int lbaNum = Integer.parseInt(lba);
            if(isLbaOutOfRange(lbaNum))
                throw new IllegalArgumentException("Invalid Argument. 1 번째 인자가 유효하지 않습니다.");
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid Argument. 1 번째 인자가 유효하지 않습니다.");
        }
    }

    private static boolean isLbaOutOfRange(int param) {
        return param > MAX_LBA || param < MIN_LBA;
    }

    public void printError(Exception e) {
        System.out.println(e.getMessage());
    }

    @Override
    public void fullread() {

    }
}
