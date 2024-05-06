package cores;

public interface SSDConstraint {

    String VALUE_FORMAT_REGEX = "^0x[0-9A-Fa-f]{8}$";

    String VALUE_FORMAT_EXCEPTION_MSG = "입력값 형식이 잘못되었습니다.";
    String ADDRESS_FORMAT_EXCEPTION_MSG = "주소값이 잘못되었습니다.";

    int MAX_BOUNDARY = 100;
    int MIN_BOUNDARY = 0;
}
