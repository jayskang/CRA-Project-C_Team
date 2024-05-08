import command.Commander;
import read.ReadModule;
import write.WriteModule;

public class Ssd {
    public static void main(String[] args) {
        Commander commander = new Commander(args, new ReadModule(), new WriteModule());
        commander.runCommand();
    }
}