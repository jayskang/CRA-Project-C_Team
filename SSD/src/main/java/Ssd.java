import command.Commander;
import erase.EraseModule;
import read.ReadModule;
import write.WriteModule;

public class Ssd {
    public static void main(String[] args) {
        Commander commander = new Commander(args);
        commander.runCommand();
    }
}