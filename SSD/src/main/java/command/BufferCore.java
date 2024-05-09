package command;

public interface BufferCore {

    String BUFFER_FILE_NAME = "buffer.txt";

    void flush();
    boolean hit(Commander command);
    void push(Commander command);
}
