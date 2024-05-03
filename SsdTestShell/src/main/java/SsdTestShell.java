public class SsdTestShell {
    private SSD ssd;

    public void setSsd(SSD ssd) {
        this.ssd = ssd;
    }

    public void read(String lba) {
        try{
            ssd.read(lba);
        } catch (IllegalArgumentException e){
            System.out.println("INVALID Argument." + e.getMessage());
        }
    }
}
