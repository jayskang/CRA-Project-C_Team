package write;

import cores.AddressConstraint;

public class WriteModule implements WriteCore {

    private final SsdFileWriter fileWriter;

    public WriteModule() {
        this.fileWriter = new SsdFileWriter();
    }

    private int convertHexToUnsignedInt(String value) {
        try {
            String inputValue = value.substring(2);

            return Integer.parseUnsignedInt(inputValue, 16);
        } catch (NumberFormatException numberFormatException) {
            return -1;
        }
    }

    private boolean checkAddressBoundary(int address) {
        return address >= AddressConstraint.MIN_BOUNDARY && AddressConstraint.MAX_BOUNDARY > address;
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
}
