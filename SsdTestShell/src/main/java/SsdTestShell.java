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
            ssd.write(lba, data);
        } catch(IllegalArgumentException e) {
            printError(e);
        }
    }
    @Override
    public void read(String lba) {
        int param = -1;
        try{
            param = Integer.parseInt(lba);
            if(param > MAX_LBA || param < MIN_LBA)
                throw new NumberFormatException();
            ssd.read(lba);
            System.out.println("read success: "+ lba);
        } catch (NumberFormatException e){
            printError(e);
        }
    }

    @Override
    public void fullwrite(String data) {

    }

    @Override
    public void fullread() {

    }

    public void printError(Exception e) {
        System.out.println(e.getMessage());
    }
}
