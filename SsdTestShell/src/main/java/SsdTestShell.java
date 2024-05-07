import java.util.ArrayList;

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
//            printError(e);
        }
    }
    @Override
    public String read(String lba) throws IllegalArgumentException {
        try{
            int param = Integer.parseInt(lba);
            if(inInvalidLbaValue(param))
                throw new NumberFormatException("INVALID COMMAND");
            return ssd.read(lba);
        } catch (NumberFormatException e){
            throw new IllegalArgumentException("INVALID COMMAND");
        }
    }

    private boolean inInvalidLbaValue(int param) {
        return param > MAX_LBA || param < MIN_LBA;
    }

    @Override
    public void fullwrite(String data) {

    }

    @Override
    public ArrayList<String> fullread() throws IllegalArgumentException {
        return null;
    }
}
