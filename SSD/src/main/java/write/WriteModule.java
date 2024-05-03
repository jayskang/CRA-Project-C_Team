package write;

public class WriteModule {

    private NandWriteModule nandWriteModule;

    public WriteModule() {
        this.nandWriteModule = new NandWriteModule();
    }

    private int convertHexToUnsignedInt(String value) {
        String inputValue = value.substring(2);
        int convertedValue = Integer.parseUnsignedInt(inputValue, 16);
        return convertedValue;
    }

    public void write(int address, String value) {
        int convertedValue = convertHexToUnsignedInt(value);
        this.nandWriteModule.store(address, convertedValue);
    }
}
