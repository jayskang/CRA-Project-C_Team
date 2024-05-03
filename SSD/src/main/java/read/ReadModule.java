package read;

import static java.lang.Integer.parseInt;

public class ReadModule {

    public void read(String str) {
        String command[] = str.split(" ");
        if(parseInt(command[2])>100){
            throw new RuntimeException("주소 입력이 잘못되었습니다.");
        }
    }
}
