public class SsdTestShell {
    public static final int MAX_LBA = 99;
    public static final int MIN_LBA = 0;
    private SSD ssd;

    public void setSsd(SSD comm) {
        this.ssd = comm;
    }

    public void read(String lba) {
        try{
            int intLba = Integer.parseInt(lba);
            if(intLba > MAX_LBA || intLba < MIN_LBA) {
                System.out.println("Wrong Lba. Try again.");
                return;
            }
            ssd.read(intLba);
        } catch (NumberFormatException e){
            System.out.println("Wrong Lba. Try again.");
        }
    }
}
