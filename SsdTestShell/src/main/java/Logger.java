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

import static constants.Logging.*;

public class Logger {
    private static BufferedWriter bw;
    private static Logger logger;
    private static File file;

    private Logger() throws IOException {
        file = new File(LATEST_LOGFILE_NAME);
        bw = new BufferedWriter(new FileWriter(file));
    }

    public static Logger makeLog() throws IOException {
        if (logger == null) logger = new Logger();
        return logger;
    }

    public void print(String errMessage) throws IOException {
        StackTraceElement calledThread = Thread.currentThread().getStackTrace()[2];

        printAndWriteLog(getLogMessage(errMessage, calledThread));

        if (isOver10KB()) {
            seperateFileForManagingFileSize();
            checkFileNumberAndZipOldestFile();
        }
    }

    private static String getLogMessage(String errMessage, StackTraceElement calledThread) {
        return setDateForLogging() + " " + calledThread.getClassName() + "." +
                calledThread.getMethodName() + "()" + "\t" + errMessage;
    }

    private static String setDateForLogging() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("'" + LOGGING_DATE_FORMAT));
    }

    private static void printAndWriteLog(String logMessage) throws IOException {
        System.out.println(logMessage);
        bw.write(logMessage + "\n");
        bw.flush();
    }

    private static boolean isOver10KB() {
        return file.length() / BYTES >= MAX_FILE_SIZE;
    }

    private static void seperateFileForManagingFileSize() throws IOException {
        bw.close();
        File renameFile = new File(setNewLogfileName());
        file.renameTo(renameFile);
        bw = new BufferedWriter(new FileWriter(LATEST_LOGFILE_NAME, true));
    }

    private static String setNewLogfileName() {
        return SEPERATED_LOGFILE_PREFIX
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern(FILE_NAME_DATE_FORMATTER))
                + FILE_TYPE_LOG;
    }

    private static void checkFileNumberAndZipOldestFile() {
        List<File> fileList = getExistsFileList();
        if (isMoreThan2Files(fileList)) {
            zipOldestFile(fileList);
        }
    }

    private static List<File> getExistsFileList() {
        File directory = new File(FILE_PATH);
        File[] files = directory.listFiles(
                (dir, name) -> name.startsWith(SEPERATED_LOGFILE_PREFIX) && name.endsWith(FILE_TYPE_LOG));
        List<File> fileList = new ArrayList<>();
        if (files != null) {
            for (File fileObject : files) {
                if (fileObject.isFile()) {
                    fileList.add(fileObject);
                }
            }
            Collections.sort(fileList, Comparator.comparing(File::getName));
        }
        return fileList;
    }

    private static boolean isMoreThan2Files(List<File> fileList) {
        return fileList.size() >= MAX_LOGFILE_NUMBER;
    }

    private static void zipOldestFile(List<File> fileList) {
        String oldestFileName = fileList.get(0).getName();
        File oldestFile = new File(oldestFileName);
        oldestFile.renameTo(new File(oldestFileName.replace(FILE_TYPE_LOG, FILE_TYPE_ZIP)));
    }

}





