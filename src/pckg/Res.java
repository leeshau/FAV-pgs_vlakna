package pckg;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Res {

    /**the complete number of threads in the whole run*/
    public static HashMap<String, String> conf = new HashMap<>();

    public static ExecutorService POOL_PMAN;
    public static ExecutorService POOL_CMAN;
    public static ExecutorService POOL_BMAN;
    public static ExecutorService POOL_VMAN;


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
        init_pools();
        return true;
    }

    private static void init_pools() {
        Res.POOL_PMAN = Executors.newFixedThreadPool(Integer.parseInt( Res.conf.get("POOL_PMAN") ));
        Res.POOL_CMAN = Executors.newFixedThreadPool(Integer.parseInt( Res.conf.get("POOL_CMAN") ));
        Res.POOL_BMAN = Executors.newFixedThreadPool(Integer.parseInt( Res.conf.get("POOL_BMAN") ));
        Res.POOL_VMAN = Executors.newFixedThreadPool(Integer.parseInt( Res.conf.get("POOL_VMAN") ));
    }


    public static void shutdown_pools() {
        Res.POOL_PMAN.shutdown();
        Res.POOL_CMAN.shutdown();
        Res.POOL_BMAN.shutdown();
        Res.POOL_VMAN.shutdown();
    }
}
