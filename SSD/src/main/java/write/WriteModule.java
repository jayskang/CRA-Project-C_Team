package write;

import command.Buffer;
import command.Commander;
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
    public void write(int lba, String value) {
        if (checkValueFormat(value) && this.checkLbaBoundary(lba)) {
            int convertedValue = convertHexToUnsignedInt(value);
            long unsignedValue = Long.parseLong(Integer.toUnsignedString(convertedValue));

            if (unsignedValue >= 0) {
                this.fileWriter.store(lba, value);
            }
        }
    }

    @Override
    public void bufferWrite(int lba, String value) {
        Buffer buffer = Buffer.getInstance();
        buffer.push(new Commander(new String[]{"W", String.valueOf(lba), value}, null, new WriteModule(), null));
    }
}
