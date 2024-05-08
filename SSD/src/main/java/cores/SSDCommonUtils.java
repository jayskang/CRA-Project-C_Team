package cores;

import static cores.SSDConstraint.MAX_BOUNDARY;
import static cores.SSDConstraint.MIN_BOUNDARY;

public class SSDCommonUtils {

    protected boolean checkLbaBoundary(int lba) {
        return MIN_BOUNDARY <= lba && lba < MAX_BOUNDARY;
    }
}
