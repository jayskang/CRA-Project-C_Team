package erase;

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
        return 0 < size && size <= 10;
    }

    @Override
    public void E(int lba, int size) {
        if(this.checkLbaBoundary(lba) && checkValidSize(size)) {
            for(int startLba = lba; startLba < MAX_BOUNDARY; startLba += 1) {
                fileWriter.store(startLba, DEFAULT_VALUE);
            }
        }
    }
}
