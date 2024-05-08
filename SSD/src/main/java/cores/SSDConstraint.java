package cores;

public interface SSDConstraint {

    String VALUE_FORMAT_REGEX = "^0x[0-9A-Fa-f]{8}$";
    String NAND_FILENAME = "nand.txt";
    String RESULT_FILENAME = "result.txt";

    int NO_OPERATION_MIN_ERASE_SIZE = 0;
    int MAX_ERASE_SIZE = 10;

    String DEFAULT_VALUE = "0x00000000";

    int MAX_BOUNDARY = 100;
    int MIN_BOUNDARY = 0;
}
