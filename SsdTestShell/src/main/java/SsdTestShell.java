public class SsdTestShell implements ISsdCommand{
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
        try{
            ssd.read(lba);
        } catch (IllegalArgumentException e){
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
