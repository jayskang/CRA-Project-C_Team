import java.io.IOException;
import java.util.ArrayList;

public interface ISsdTestShell {
    void write(String lbs, String data);
    String read(String lbs) throws IOException;
    void fullwrite(String data);
    ArrayList<String> fullread() throws IOException;
}
