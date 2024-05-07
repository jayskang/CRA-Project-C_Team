package cores;

public interface SSDConstraint {

    String VALUE_FORMAT_REGEX = "^0x[0-9A-Fa-f]{8}$";
    String FILENAME = "nand.txt";

    String NAND_ABSOLUTE_LOCATION = "src/main/resources/";
    String INITIAL_STATE = "0x00000000";

    int MAX_BOUNDARY = 100;
    int MIN_BOUNDARY = 0;
}
