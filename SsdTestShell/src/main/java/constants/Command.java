package constants;

public class Command {
    // SSD 프로그램 실행관련 Constants
    public static final String SSD_EXEC_JAVA_COMMAND = "java";
    public static final String SSD_EXEC_JAR_OPTION = "-jar";
    public static final String SSD_EXEC_JAR_FILE_PATH = "./ssd.jar";
    public static final String SSD_READ_OPTION_CMD = "R";
    public static final String SSD_WRITE_OPTION_CMD = "W";
    public static final String SSD_ERASE_OPTION_CMD = "E";
    public static final String SSD_FLUSH_OPTION_CMD = "F";

    public static final String RESULT_FILE_PATH = "./result.txt";

    // 명령 유효성 관련 Constants
    public static final int MAX_LBA = 99;
    public static final int MIN_LBA = 0;
    public static final String VALUE_FORMAT_REGEX = "^0x[0-9A-Fa-f]{8}$";
}
