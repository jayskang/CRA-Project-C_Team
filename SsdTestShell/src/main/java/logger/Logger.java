package logger;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static constants.Logging.*;

public class Logger {
    private static Logger logger;
    private static FileChannel fileChannel;
    private static String beforeUntilLogFileName;
    private static int untilFileSeqNum = 1;
    public static final int LOG_METHOD_NAME_MAX_SIZE = 40;

    private Logger() throws IOException {
        beforeUntilLogFileName = LATEST_LOGFILE_NAME;
        fileChannel = FileChannel.open(
                Paths.get(LATEST_LOGFILE_NAME)
                , StandardOpenOption.CREATE
                , StandardOpenOption.WRITE);
    }

    public static Logger makeLog() throws IOException {
        if (logger == null) logger = new Logger();
        return logger;
    }

    public void print(String errMessage) throws IOException {
        StackTraceElement calledThread = Thread.currentThread().getStackTrace()[2];
        printAndWriteLog(getLogMessage(errMessage, calledThread));

//        if (isOver10KB()) {
//            seperateFileForManagingFileSize_1();
//            checkFileNumberAndZipOldestFile();
//        }
    }

    private static void printAndWriteLog(String logMessage) throws IOException {
        StringBuffer sb = new StringBuffer(logMessage + "\n");

        if (isOver10KB(sb)) {
            fileChannel.close();
            try {
                Path oldfile = Paths.get(LATEST_LOGFILE_NAME);
                Path newfile = Paths.get(getNewLogfileName());
                Files.move(oldfile, newfile);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            fileChannel = FileChannel.open(
                    Paths.get(LATEST_LOGFILE_NAME)
                    , StandardOpenOption.CREATE
                    , StandardOpenOption.WRITE);
        }

        ByteBuffer buffer = StandardCharsets.UTF_8.encode(sb.toString());

        fileChannel.write(buffer);

        checkFileNumberAndZipOldestFile();

    }

    private static boolean isOver10KB(StringBuffer sb) throws IOException {
        return (fileChannel.size() + sb.toString().getBytes().length) / BYTES >= MAX_FILE_SIZE;
    }

    private static String getLogMessage(String errMessage, StackTraceElement calledThread) {
        return getDateForLogging() + " " + getFormattedMethodName(calledThread) + errMessage;
    }

    private static String getFormattedMethodName(StackTraceElement calledThread) {
        return String.format("%-" + LOG_METHOD_NAME_MAX_SIZE + "s", getMethodName(calledThread)).replace(" ", " ");
    }

    private static String getMethodName(StackTraceElement calledThread) {
        String methodName = calledThread.getClassName() + "." + calledThread.getMethodName() + "()";
        if (methodName.length() >= LOG_METHOD_NAME_MAX_SIZE) {
            methodName = methodName.substring(0, LOG_METHOD_NAME_MAX_SIZE);
        }
        return methodName;
    }

    private static String getDateForLogging() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("'" + LOGGING_DATE_FORMAT));
    }


    private static String getNewLogfileName() {
        StringBuilder newSb = new StringBuilder();
        newSb.append(SEPERATED_LOGFILE_PREFIX)
                .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern(FILE_NAME_DATE_FORMATTER)))
                .append(FILE_TYPE_LOG);

        if (newSb.toString().equals(beforeUntilLogFileName)) {
            newSb.append(".").append(untilFileSeqNum++);
        }
        beforeUntilLogFileName = newSb.toString();
        return newSb.toString();
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





