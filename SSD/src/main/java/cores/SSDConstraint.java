package cores;

public interface SSDConstraint {

    String VALUE_FORMAT_REGEX = "^0x[0-9A-Fa-f]{8}$";
    String FILENAME = "nand.txt";

    int MAX_BOUNDARY = 100;
    int MIN_BOUNDARY = 0;

    String NAND_FILENAME = "nand.txt";
    String RESULT_FILENAME = "result.txt";

    String DEFAULT_VALUE = "0x00000000";


}
