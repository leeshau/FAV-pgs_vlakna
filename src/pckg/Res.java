package pckg;

import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

public class Res {

    /**the complete number of threads in the whole run*/
    public static final int WAIT_MILIS = 500;

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


}
