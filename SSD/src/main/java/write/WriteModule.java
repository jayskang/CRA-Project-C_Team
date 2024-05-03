package write;

import cores.AddressConstraint;

public class WriteModule implements WriteCore {

    private final SsdFileWriter fileWriter;
    private Exception errorLog;

    public WriteModule() {
        this.fileWriter = new SsdFileWriter();
    }

    private int convertHexToUnsignedInt(String value) {
        try {
            if(!value.contains("0x")) {
                throw new NumberFormatException();
            }
            String inputValue = value.substring(2);
            return Integer.parseUnsignedInt(inputValue, 16);
        } catch (NumberFormatException numberFormatException) {
            this.errorLog = numberFormatException;
            return -1;
        }
    }

    private boolean checkAddressBoundary(int address) {
        try {
            if (AddressConstraint.MIN_BOUNDARY <= address && address < AddressConstraint.MAX_BOUNDARY) {
                this.errorLog = null;
                return true;
            }
            throw new IllegalArgumentException();
        } catch (IllegalArgumentException illegalArgumentException) {
            this.errorLog = illegalArgumentException;
            return false;
        }
    }

    @Override
    public void write(int address, String value) {
        if(checkAddressBoundary(address)) {
            int convertedValue = convertHexToUnsignedInt(value);

            if(convertedValue >= 0) {
                this.errorLog = null;
                this.fileWriter.store(address, convertedValue);
            }
        }
    }

    public Exception getErrorLog() {
        return this.errorLog;
    }
}
