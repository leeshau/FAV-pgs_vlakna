package pckg;

import pckg.workers.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
public class Main {

    public static void main(String[] args){
        System.out.println("necum");
        try {
            String content = new String(Files.readAllBytes(Paths.get(args[0])), StandardCharsets.UTF_8);
            AllMan am = new AllMan(null, "am1", content);
            Thread t = new Thread(am);
            t.start();
            t.join();
        } catch (IOException e) {
            System.out.println("Couldn't read the file. Shutting down.");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("Could not join");
            e.printStackTrace();
        }
    }

}
