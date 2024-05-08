import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class Logger {
    private static BufferedWriter bw;
    private static Logger logger;
    private static File file;

    private Logger() throws IOException {
        file = new File("latest.log");
        bw = new BufferedWriter(new FileWriter(file));
    }

    public static Logger makeLog() throws IOException {
        if (logger == null) logger = new Logger();
        return logger;
    }

    public void logging(String str) throws IOException {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyyMMdd_HH'h'_mm'm'_ss");
        String newFileName = "until_" + now.format(formatter1) + ".log";

        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("'['yy/MM/dd HH:mm']'");
        String formatedNow2 = now.format(formatter2);


        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        String methodName = "";
        if (stackTraceElements.length > 2) {
            methodName = stackTraceElements[2].getClassName() +"."+stackTraceElements[2].getMethodName();
        }

        String log = formatedNow2 + " " + methodName + "\t" + str;
        bw.write(log + "\n");
        System.out.println(log);
        bw.flush();

        long kilobyte= file.length() / 1024;

        if (kilobyte >= 10) {
            bw.close();
            File renameFile = new File(newFileName);
            file.renameTo(renameFile);

            bw = new BufferedWriter(new FileWriter("latest.log"));

            File directory = new File("./");
            File[] files = directory.listFiles((dir, name) -> name.startsWith("until_") && name.endsWith(".log"));
            List<File> fileList = new ArrayList<>();
            if (files != null) {
                for (File fileObject : files) {
                    if (fileObject.isFile()) {
                        fileList.add(fileObject);
                    }
                }
                Collections.sort(fileList, Comparator.comparing(File::getName));
            }
            if (fileList.size() >= 2) {
                String fileOldName = fileList.get(0).getName();
                String fileNewName = fileOldName.replace("log", "zip");
                File renameFile2 = new File(fileNewName);
                File oldFile1 = new File(fileOldName);
                oldFile1.renameTo(renameFile2);
            }
        }
    }
}





