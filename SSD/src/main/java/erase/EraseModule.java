package erase;

import command.Buffer;
import command.CommandFactory;
import cores.SSDCommonUtils;
import write.SsdFileWriter;

import static cores.SSDConstraint.*;

public class EraseModule extends SSDCommonUtils implements EraseCore {

    private final SsdFileWriter fileWriter;

    public EraseModule() {
        super();
        this.fileWriter = new SsdFileWriter();
    }

    private boolean checkValidSize(int size) {
        return NO_OPERATION_MIN_ERASE_SIZE < size && size <= MAX_ERASE_SIZE;
    }

    @Override
    public void erase(int lba, int size) {
        if (this.checkLbaBoundary(lba) && checkValidSize(size)) {
            for (int startLba = lba; startLba < MAX_BOUNDARY && startLba < lba + size; startLba += 1) {
                fileWriter.store(startLba, DEFAULT_VALUE);
            }
        }
    }

    @Override
    public void bufferErase(int lba, int size) {
        Buffer buffer = Buffer.getInstance();

        if (this.checkLbaBoundary(lba) && checkValidSize(size)) {
            buffer.push(CommandFactory.getCommand(new String[]{"E", String.valueOf(lba), String.valueOf(size)}));
        }
    }
}
