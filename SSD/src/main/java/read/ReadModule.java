package read;

import cores.AddressConstraint;
import cores.ExceptionMessage;

import static java.lang.Integer.parseInt;

class RequestFailException extends RuntimeException {
    public RequestFailException(String message) {
        super(message);
    }
}

public class ReadModule implements ReadCore {

    public void read(String request) {
        String command[] = request.split(" ");

        if(isValidAddress(command)){
            throw new IllegalArgumentException(ExceptionMessage.ILLEGAL_ADDRESS_VALUE_EXCEPTION_MSG);
        }
    }

    private static boolean isValidAddress(String[] command) {
        return parseInt(command[2]) >= AddressConstraint.MAX_BOUNDARY || parseInt(command[2]) <AddressConstraint.MIN_BOUNDARY;
    }
}
