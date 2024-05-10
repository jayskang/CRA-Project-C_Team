package write;

import command.Buffer;
import command.CommandFactory;
import command.Commander;
import cores.SSDCommonUtils;
import cores.SSDConstraint;

public class WriteModule extends SSDCommonUtils implements WriteCore {

    private final SsdFileWriter fileWriter;

    public WriteModule() {
        super();
        this.fileWriter = new SsdFileWriter();
    }

    @Override
    public void write(int lba, String value) {
        if (isValidValueFormatAndLbaBoundary(lba, value)) {
            if (isValueOver0(value)) {
                this.fileWriter.store(lba, value);
            }
        }
    }

    private boolean isValidValueFormatAndLbaBoundary(int lba, String value) {
        return checkValueFormat(value) && this.checkLbaBoundary(lba);
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

    private boolean isValueOver0(String value) {
        int convertedValue = convertHexToUnsignedInt(value);
        long unsignedValue = Long.parseLong(Integer.toUnsignedString(convertedValue));
        return unsignedValue >= 0;
    }

    @Override
    public void bufferWrite(int lba, String value) {
        Buffer buffer = Buffer.getInstance();
        buffer.push(CommandFactory.getCommand(new String[]{"W", String.valueOf(lba), value}));
    }
}
