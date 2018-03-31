package pdm.h2.demo.objects;

import java.io.IOException;

public class cls {
    public static void main(String... arg) throws IOException, InterruptedException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
    }
}
