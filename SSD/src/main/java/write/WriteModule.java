package write;

import cores.AddressConstraint;
import cores.ExceptionMessage;

public class WriteModule implements WriteCore {

    private final SsdFileWriter fileWriter;

    public WriteModule(SsdFileWriter fileWriter) {
        this.fileWriter = fileWriter;
    }

    private int convertHexToUnsignedInt(String value) {
        try {
            String inputValue = value.substring(2);

            return Integer.parseUnsignedInt(inputValue, 16);
        } catch (NumberFormatException numberFormatException) {
            throw new IllegalArgumentException(ExceptionMessage.ILLEGAL_STORE_TO_VALUE_EXCEPTION_MSG);
        }
    }

    private boolean checkAddressBoundary(int address) {
        if(address < AddressConstraint.MIN_BOUNDARY || AddressConstraint.MAX_BOUNDARY <= address) {
            throw new IllegalArgumentException(ExceptionMessage.ILLEGAL_ADDRESS_VALUE_EXCEPTION_MSG);
        }
        return true;
    }

    @Override
    public void write(int address, String value) {
        if(checkAddressBoundary(address)) {
            int convertedValue = convertHexToUnsignedInt(value);

            this.fileWriter.store(address, convertedValue);
        }
    }
}
