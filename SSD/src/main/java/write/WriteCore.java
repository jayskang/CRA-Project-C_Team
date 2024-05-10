package write;

public interface WriteCore {

    void bufferWrite(int lba, String value);
    void write(int lba, String value);
}
