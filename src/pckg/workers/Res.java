package pckg.workers;

import java.util.HashMap;
import java.util.Map;

public class Res {

    /**the complete number of threads in the whole run*/
    static int thread_count = 0;
    static final int THREAD_LIMIT = 5;
    static final int WAIT_MILIS = 500;
    static boolean process_text_running = false;


    static void print_map(HashMap<String, Integer> map) {

//         FileWriter fstream = new FileWriter(new File(".").getAbsolutePath()+"//folder//out.txt",true);

        for(Map.Entry<String, Integer> entry : map.entrySet()){
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

    }
}
