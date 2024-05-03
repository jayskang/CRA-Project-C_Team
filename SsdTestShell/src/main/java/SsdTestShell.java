public class SsdTestShell {
    SSD ssd;

    void setSsd(SSD ssd) {
        this.ssd = ssd;
    }

    void write(String lba, String data) {
        try {
            ssd.write(lba, data);
        } catch(IllegalArgumentException e) {
            printError(e);
        }
    }

    void printError(Exception e) {
        System.out.println(e.getMessage());
    }
}
