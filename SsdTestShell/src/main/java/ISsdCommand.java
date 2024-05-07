import java.util.ArrayList;

public interface ISsdCommand {
    void write(String lbs, String data);
    String read(String lbs) throws IllegalArgumentException;
    void fullwrite(String data);
    ArrayList<String> fullread() throws IllegalArgumentException;
}
