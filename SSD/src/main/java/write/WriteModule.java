package write;

import cores.SSDCommonUtils;
import cores.SSDConstraint;

public class WriteModule extends SSDCommonUtils implements WriteCore {

    private final SsdFileWriter fileWriter;

    public WriteModule() {
        super();
        this.fileWriter = new SsdFileWriter();
    }

    private boolean checkValueFormat(String value) {
        return value.matches(SSDConstraint.VALUE_FORMAT_REGEX);
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
        if (checkValueFormat(value) && this.checkAddressBoundary(address)) {
            int convertedValue = convertHexToUnsignedInt(value);

            if (convertedValue >= 0) {
                this.fileWriter.store(address, value);
            }
        }
    }
}
