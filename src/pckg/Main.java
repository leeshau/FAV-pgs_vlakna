package pckg;

import pckg.workers.*;

import java.awt.print.Book;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args){
        System.out.println("necum");
        try {
            String content = new String(Files.readAllBytes(Paths.get(args[0])), StandardCharsets.UTF_8);
//            ParagraphMan pm = new ParagraphMan(null, "pm1", content);
//            ChapterMan cm = new ChapterMan(null, "pm1", content);
//            BookMan bm = new BookMan(null, "bm1", content);
//            VolumeMan vm = new VolumeMan(null, "bm1", content);
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
