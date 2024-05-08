package write;

import cores.SSDConstraint;

public class WriteModule implements WriteCore, EraseCore {

    private final SsdFileWriter fileWriter;

    public WriteModule() {
        super();
        this.fileWriter = new SsdFileWriter();
    }

    private boolean checkValueFormat(String value) {
        return value.matches(SSDConstraint.VALUE_FORMAT_REGEX);
    }

    private boolean checkAddressBoundary(int lba) {
        return SSDConstraint.MIN_BOUNDARY <= lba && lba < SSDConstraint.MAX_BOUNDARY;
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
    public void write(int lba, String value) {
        if (checkValueFormat(value) && checkAddressBoundary(lba)) {
            int convertedValue = convertHexToUnsignedInt(value);
            long unsignedValue = Long.parseLong(Integer.toUnsignedString(convertedValue));

            if (unsignedValue >= 0) {
                this.fileWriter.store(lba, value);
            }
        }
    }

    @Override
    public void E(int lba, int size) {

    }
}
