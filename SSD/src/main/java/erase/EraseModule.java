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

    @Override
    public void E(int lba, int size) {
        if(this.checkLbaBoundary(lba)) {
            for(int startLba = lba; startLba < MAX_BOUNDARY; startLba += 1) {
                fileWriter.store(startLba, DEFAULT_VALUE);
            }
        }
    }
}
