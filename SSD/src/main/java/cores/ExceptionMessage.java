package cores;

public interface ExceptionMessage {
    String ILLEGAL_STORE_TO_VALUE_EXCEPTION_MSG = "매개변수 형식 오류:"
            + "문자열이 '0x'로 시작하며 8개의 16진수만 포함하는지 확인해 주세요.";
    String ILLEGAL_ADDRESS_VALUE_EXCEPTION_MSG = "매개변수 형식 오류:"
            + "주소값은 0에서 100까지만 허용됩니다.";
}
