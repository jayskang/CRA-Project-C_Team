package erase;

public interface EraseCore {
    void erase(int lba, int size);
    void bufferErase(int lba, int size);
}
