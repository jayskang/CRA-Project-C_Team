package write;

import cores.SSDConstraint;

public class WriteModule implements WriteCore {

    private final SsdFileWriter fileWriter;

    public WriteModule() {
        super();
        this.fileWriter = new SsdFileWriter();
    }

    private boolean checkValueFormat(String value) {
        return value.matches(SSDConstraint.VALUE_FORMAT_REGEX);
    }

    private boolean checkAddressBoundary(int address) {
        return SSDConstraint.MIN_BOUNDARY <= address && address < SSDConstraint.MAX_BOUNDARY;
    }

    private int convertHexToUnsignedInt(String value) {
        try {
            String inputValue = value.substring(2);
            return Integer.parseUnsignedInt(inputValue, 16);
        } catch (NumberFormatException numberFormatException) {
            return -1;
        }
    }

    @Override
    public void write(int address, String value) {
        if (checkValueFormat(value) && checkAddressBoundary(address)) {
            int convertedValue = convertHexToUnsignedInt(value);

            if (convertedValue >= 0) {
                this.fileWriter.store(address, value);
            }
        }
    }
}
