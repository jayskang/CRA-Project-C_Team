package read;

import cores.AddressConstraint;
import static java.lang.Integer.parseInt;

class RequestFailException extends RuntimeException {
    public RequestFailException(String message) {
        super(message);
    }
}

public class ReadModule implements AddressConstraint {

    public void read(String request) {
        String command[] = request.split(" ");

        if(isValidAddress(command)){
            throw new RequestFailException("주소 입력이 잘못되었습니다.");
        }
    }

    private static boolean isValidAddress(String[] command) {
        return parseInt(command[2]) > MAX_BOUNDARY;
    }
}
