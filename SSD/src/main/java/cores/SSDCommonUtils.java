package cores;

public abstract class SSDCommonUtils {

    public SSDCommonUtils() {
    }

    protected boolean checkAddressBoundary(int address) {
        return SSDConstraint.MIN_BOUNDARY <= address && address < SSDConstraint.MAX_BOUNDARY;
    }
}
