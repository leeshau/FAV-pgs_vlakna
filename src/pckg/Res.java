package pckg;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.Semaphore;

public class Res {

    /**the complete number of threads in the whole run*/
    public static HashMap<String, String> conf = new HashMap<>();

    public static Semaphore SEM_PMAN;
    public static Semaphore SEM_CMAN;
    public static Semaphore SEM_BMAN;
    public static Semaphore SEM_VMAN;

    public static int thread_count_paragraphman = 0;
    public static final int LIMIT_PARAGRAPHMAN = 5;
    public static boolean process_text_paragraphman_running = false;

    public static int thread_count_chapterman = 0;
    public static final int LIMIT_CHAPTERMAN = 5;
    public static boolean process_text_chapterman_running = false;


    public static void print_map(HashMap<String, Integer> map) {
//         FileWriter fstream = new FileWriter(new File(".").getAbsolutePath()+"//folder//out.txt",true);
        SortedSet<String> keys = new TreeSet<>(map.keySet());
        for(String key : keys){
            System.out.println(key + ": " + map.get(key));
        }

    }

    static boolean load_config(){
        try {
            Scanner sc = new Scanner(new File("config.properties"));
            while(sc.hasNext()){
                String line = sc.nextLine();
                if(line.contains("%")) continue; //comment
                String[] data = line.split(" = ");
                Res.conf.put(data[0], data[1]);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File config.properties not found.");
            return false;
        }
        init_semaphores();
        return true;
    }

    private static void init_semaphores() {
        Res.SEM_PMAN = new Semaphore(Integer.parseInt(Res.conf.get("SEM_PMAN")));
        Res.SEM_CMAN = new Semaphore(Integer.parseInt(Res.conf.get("SEM_CMAN")));
        Res.SEM_BMAN = new Semaphore(Integer.parseInt(Res.conf.get("SEM_BMAN")));
        Res.SEM_VMAN = new Semaphore(Integer.parseInt(Res.conf.get("SEM_VMAN")));
    }


}
