package write;

import cores.SSDConstraint;
import read.SsdFileReader;

import java.io.*;

public class SsdFileWriter {

    private SsdFileReader reader;
    private BufferedWriter bufferedWriter;

    public SsdFileWriter() {
        File file = new File("src/main/resources/nand.txt");

        if (!file.exists()) {
            try {
                boolean hasCreateDir = file.getParentFile().mkdirs(); // 상위 디렉토리가 없으면 생성
                boolean hasNewFile = file.createNewFile(); // 파일 생성
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

//        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        try {
            this.bufferedWriter = new BufferedWriter(new FileWriter(file));
            StringBuilder stringBuilder;

            for (int i = 0; i < SSDConstraint.MAX_BOUNDARY; i += 1) {
                stringBuilder = new StringBuilder();

                stringBuilder.append(i);
                stringBuilder.append(" ");
                stringBuilder.append("0x00000000");
                stringBuilder.append("\n");

                this.bufferedWriter.write(stringBuilder.toString());
            }
            this.bufferedWriter.close();
        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
        }
    }

    public void store(int address, String value) {
        try {
            String[] nand = this.reader.readFile();

            nand[address] = value;

            File file = new File("src/main/resources/nand.txt");

            this.bufferedWriter = new BufferedWriter(new FileWriter(file));
            StringBuilder stringBuilder;

            if (!file.exists()) {
                try {
                    boolean hasCreateDir = file.getParentFile().mkdirs(); // 상위 디렉토리가 없으면 생성
                    boolean hasNewFile = file.createNewFile(); // 파일 생성
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }

            for(int i = 0; i < SSDConstraint.MAX_BOUNDARY; i += 1) {

                stringBuilder = new StringBuilder();

                stringBuilder.append(i);
                stringBuilder.append(" ");
                stringBuilder.append("0x00000000");
                stringBuilder.append("\n");

                this.bufferedWriter.write(stringBuilder.toString());
            }
            this.bufferedWriter.close();
        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
        }
    }
}
