package cores;

public interface SSDConstraint {

    String VALUE_FORMAT_REGEX = "^0x[0-9A-Fa-f]{8}$";

    int MAX_BOUNDARY = 100;
    int MIN_BOUNDARY = 0;
}
