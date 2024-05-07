public class SsdTestShell implements ISsdCommand{
    public static final int MAX_LBA = 99;
    public static final int MIN_LBA = 0;
    private final String VALUE_FORMAT_REGEX = "^0x[0-9A-Fa-f]{8}$";
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
    public void read(String lba) {
        try {
            checkIsLbaValid(lba);
            ssd.read(lba);
            System.out.println("read success: " + lba);
        } catch(Exception e) {

        }
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
            throw new IllegalArgumentException("INVALID Argument. 2번째 인자가 유효하지 않습니다.");
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
    public void fullread() {

    }
}
