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

    private static void checkIsDataValid(String data) throws IllegalArgumentException {
        boolean invalidDataFormat = !data.startsWith("0x") || data.length() != 10;
        if (invalidDataFormat) {
            throw new IllegalArgumentException("INVALID Argument. 2번째 인자가 유효하지 않습니다.");
        }
        try {
            Integer.parseInt(data.substring(2), 16);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("INVALID Argument. 2번째 인자가 유효하지 않습니다.");
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

    private static void checkIsLbaValid(String lba) throws IllegalArgumentException{
        int param;
        try {
            param = Integer.parseInt(lba);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid Argument. 1 번째 인자가 유효하지 않습니다.");
        }
        boolean isLbaOutOfRange = param > MAX_LBA || param < MIN_LBA;
        if(isLbaOutOfRange)
            throw new IllegalArgumentException("Invalid Argument. 1 번째 인자가 유효하지 않습니다.");
    }

    public void printError(Exception e) {
        System.out.println(e.getMessage());
    }

    @Override
    public void fullwrite(String data) {

    }

    @Override
    public void fullread() {

    }
}
