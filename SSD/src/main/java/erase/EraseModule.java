package erase;

import cores.SSDConstraint;
import write.SsdFileWriter;

public class EraseModule implements EraseCore {

    private final SsdFileWriter fileWriter;

    public EraseModule() {
        this.fileWriter = new SsdFileWriter();
    }

    private boolean checkAddressBoundary(int address) {
        return SSDConstraint.MIN_BOUNDARY <= address && address < SSDConstraint.MAX_BOUNDARY;
    }

    @Override
    public void E(int lba, int size) {
        if(checkAddressBoundary(lba)) {
            for(int startLba = lba; startLba < SSDConstraint.MAX_BOUNDARY; startLba += 1) {
                fileWriter.store(startLba, "0x00000000");
            }
        }
    }
}
