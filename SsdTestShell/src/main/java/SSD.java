public class SSD {

    void write(String lbs, String data) {}

    public void read(String lba) {
        // Mock으로 실제 수행되지 않음
        int param = -1;
        try {
             param = Integer.parseInt(lba);
             if(param > 99 || param < 0)
                 throw new IllegalArgumentException("INVALID Argument.");

            System.out.println("read success: "+ lba);
        } catch (NumberFormatException e){
            throw new IllegalArgumentException("INVALID Argument.");
        }
    }
}
