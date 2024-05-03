package write;

import cores.ExceptionMessage;

public class WriteModule {

    private final NandWriteModule nandWriteModule;

    public WriteModule() {
        this.nandWriteModule = new NandWriteModule();
    }

    private int convertHexToUnsignedInt(String value) {
        try {
            String inputValue = value.substring(2);

            return Integer.parseUnsignedInt(inputValue, 16);
        } catch (NumberFormatException numberFormatException) {
            throw new IllegalArgumentException(ExceptionMessage.ILLEGAL_STORE_TO_VALUE_EXCEPTION_MSG);
        }
    }

    public void write(int address, String value) {
        int convertedValue = convertHexToUnsignedInt(value);

        this.nandWriteModule.store(address, convertedValue);
    }
}
