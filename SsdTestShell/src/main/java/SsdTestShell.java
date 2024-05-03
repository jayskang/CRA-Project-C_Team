public class SsdTestShell {
    private SSD ssd;

    public void setSsd(SSD ssd) {
        this.ssd = ssd;
    }

    public void write(String lba, String data) {
        try {
            ssd.write(lba, data);
        } catch(IllegalArgumentException e) {
            printError(e);
        }
    }

    public void read(String lba) {
        try{
            ssd.read(lba);
        } catch (IllegalArgumentException e){
            printError(e);
        }
    }

    public void printError(Exception e) {
        System.out.println(e.getMessage());
    }
}
