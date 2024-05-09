package shell;

import java.io.IOException;
import java.util.ArrayList;

public interface ISsdCommand {
    void write(String lbs, String data) throws IllegalArgumentException, IOException;
    String read(String lbs) throws IllegalArgumentException, IOException;
    void fullwrite(String data) throws IllegalArgumentException, IOException;
    ArrayList<String> fullread() throws IllegalArgumentException, IOException;
    void erase(String lba, String size) throws IllegalArgumentException, IOException;
    void eraserange(String startLba, String endLba) throws IllegalArgumentException, IOException;
}
