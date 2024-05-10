package cores;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static cores.SSDConstraint.*;

public class SSDCommonUtils {

    protected boolean checkLbaBoundary(int lba) {
        return MIN_BOUNDARY <= lba && lba < MAX_BOUNDARY;
    }

    protected void writeToFile(String dstPath, String content) {
        try {
            FileWriter fileWriter = new FileWriter(dstPath, false);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException ignored) {
        }
    }
}
