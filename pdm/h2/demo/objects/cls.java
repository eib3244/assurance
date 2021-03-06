package pdm.h2.demo.objects;

import java.io.IOException;

/*
 * A simple class used to clear the screen for a cmd prompt.
 * Author: Emerson Bolha
 */
public class cls {


    public static void clear(){

        try {
            cls.main(null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String... arg) throws IOException, InterruptedException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
    }
}
