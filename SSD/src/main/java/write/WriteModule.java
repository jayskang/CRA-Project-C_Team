package write;

import cores.AddressConstraint;

public class WriteModule implements WriteCore {

    private final SsdFileWriter fileWriter;
    private Exception exception;

    public WriteModule() {
        this.fileWriter = new SsdFileWriter();
    }

    private int convertHexToUnsignedInt(String value) {
        try {
            String inputValue = value.substring(2);

            return Integer.parseUnsignedInt(inputValue, 16);
        } catch (NumberFormatException numberFormatException) {
            this.exception = numberFormatException;
            return -1;
        }
    }

    private boolean checkAddressBoundary(int address) {
        try {
            if (AddressConstraint.MIN_BOUNDARY <= address && address < AddressConstraint.MAX_BOUNDARY) {
                return true;
            }
            throw new IllegalArgumentException();
        } catch (IllegalArgumentException illegalArgumentException) {
            this.exception = illegalArgumentException;
            return false;
        }
    }

    @Override
    public void write(int address, String value) {
        if(checkAddressBoundary(address)) {
            int convertedValue = convertHexToUnsignedInt(value);

            if(convertedValue >= 0) {
                this.fileWriter.store(address, convertedValue);
            }
        }
    }

    public Exception getErrorLog() {
        return this.exception;
    }
}
