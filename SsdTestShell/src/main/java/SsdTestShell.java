public class SsdTestShell {
    private SSD communicator;

    public void setCommunicator(SSD comm) {
        this.communicator = comm;
    }

    public void read(Integer lba) {
        if(lba > 99 || lba < 0) {
            System.out.println("Wrong Lba. Try again.");
            return;
        }
        communicator.read(lba);
    }
}
