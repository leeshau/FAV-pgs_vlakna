package pckg;

import pckg.workers.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args){
        System.out.println("Wellcome to the book-word-count program. \nBe sure to read documentation, \nset the config.properties properly \nand please be patient with bigger files, \nsome process can take up to 3 minutes.");
        if (!Res.load_config()) System.exit(1); //loading configuration failed, no need to continue
        try {
            String content = new String(Files.readAllBytes(Paths.get(args[0])), StandardCharsets.UTF_8);
            AllMan base = new AllMan(null, Res.conf.get("BOOK_NAME"), content);
            Thread t = new Thread(base);
            t.start();
            t.join();
        } catch (IOException e) {
            System.err.println("Couldn't read the input file. Shutting down.");
        } catch (InterruptedException e) {
            System.err.println("Could not join");
            e.printStackTrace();
        }
    }

}
