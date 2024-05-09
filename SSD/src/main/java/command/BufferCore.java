package command;

public interface BufferCore {

    void flush();
    boolean hit(Commander command);
    void push(Commander command);
}
