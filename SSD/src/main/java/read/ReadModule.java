package read;

import cores.AddressConstraint;
import cores.ExceptionMessage;

import static java.lang.Integer.parseInt;


public class ReadModule implements ReadCore {

    public void read(String lba) {

        if(isValidAddress(lba)){
            throw new IllegalArgumentException(ExceptionMessage.ILLEGAL_ADDRESS_VALUE_EXCEPTION_MSG);
        }
    }

    private static boolean isValidAddress(String lba) {
        return parseInt(lba) >= AddressConstraint.MAX_BOUNDARY || parseInt(lba) <AddressConstraint.MIN_BOUNDARY;
    }
}
