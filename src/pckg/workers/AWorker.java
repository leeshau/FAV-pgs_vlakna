package pckg.workers;

import pckg.Res;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

abstract class AWorker implements Runnable{

    Map<String, Integer> result_map;

    /**his upper level*/
    protected AWorker upper;
    String filename;
    String text;
    int thread_count;

    /**every class implements its way of processing the text that was passed on it
     * including the result being written to result_map*/
    protected abstract void process_text();

    AWorker(AWorker upper, String filename, String text) {
        this.upper = upper;
        String classname = this.getClass().getSimpleName();
        this.filename = classname.substring(0, classname.length() - 3) + filename;
        this.text = text;
        this.result_map = new ConcurrentHashMap<>();
//        log(this.get_full_filename(false));
    }

    /**@return full file name of the new text file to be exported
     * true, returns whole filename with no spaces and separated with '/' symbols, the last element is missing on purpose
     * false, return the content to save, whole path separated with ' - ' symbols and Ok at the end*/
        private String get_full_filename(boolean export, boolean state) {
        AWorker current_worker = this;
        LinkedList<String> names = new LinkedList<>();
        StringBuilder fullname = new StringBuilder();

        while(current_worker.upper != null){
            names.add(current_worker.filename);
            current_worker = current_worker.upper;
        }
        int names_size_fixed = names.size();
        if(export) {
            fullname.append(Res.conf.get("BOOK_NAME"));
            for (int i = names_size_fixed - 1; i >= 1; i--) {
                fullname.append(Res.conf.get("SLASH"));
                fullname.append(names.get(i).toLowerCase());
            }
            fullname.append(Res.conf.get("SLASH") + ( state ? "state.txt" : (this.filename + Res.conf.get("SLASH") + this.filename + ".txt") ));
        } else {
            for (int i = names_size_fixed - 1; i >= 0; i--) {
                if(i != names_size_fixed - 1)
                    fullname.append(" - ");
                fullname.append(names.get(i));
            }
        }
        return fullname.toString();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " " + this.filename + ": ";
    }

    void receive_result(Map<String, Integer> map, AWorker aw) {
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                String key = entry.getKey();
                int count = this.result_map.getOrDefault(key, 0);
                this.result_map.put(key, count + entry.getValue());
            }
    }

    @Override
    public void run() {
        if(this.text.matches("((\\r)?\\n)+") || this.text.length() == 0) {
            this.upper.dec_thread();
            return; //ignores empty lines
        }
        process_text();
    }

    void dec_thread() {
            this.thread_count--;
            if(this.thread_count == 0){
                this.print_res();
                print_state(null);
                if(this.upper != null) {
                    this.upper.receive_result(this.result_map, this);
                    this.upper.dec_thread();
                }
            }
    }

    /**prints files (path/state.txt) with OK statuses of analyzing*/
    private void print_state(String state){
        String path = this.get_full_filename(true, true); //complete path to a file ending with state.txt
        state = state == null ? (this.get_full_filename(false, false) + " - OK\n") : state; //parameter can be null, it is used when writing state at its core level
        try {
            File f = new File(Res.conf.get("EXPORT") + path); //only here for making the file and its directories
            synchronized (this) {
                f.getParentFile().mkdirs();
                if (!f.exists()) f.createNewFile();
                Files.write(Paths.get(Res.conf.get("EXPORT") + path), state.getBytes(), StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(this.upper != null){
            this.upper.print_state(state); //recursively writing the same state to all upper levels, state is same so there is no need to create it as we did in the first cycle here
        }
    }

    void print_res() {
        SortedSet<String> keys = new TreeSet<>(this.result_map.keySet());
        StringBuilder fin = new StringBuilder("");
        for(String key : keys){
            fin.append(key + ": " + this.result_map.get(key) + "\n");
        }
        try {
            File f = new File(Res.conf.get("EXPORT") + this.get_full_filename(true, false)); //only here for making the file and its directories
            f.getParentFile().mkdirs();
            if (!f.exists()) f.createNewFile();

            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(Res.conf.get("EXPORT") + this.get_full_filename(true, false), true)  //Set true for append mode
            );
            writer.write(fin.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void log(String s){
        System.out.println("Log: " + this.toString() + " " + s);
    }
    private void err_log(String s){
        System.err.println("Log: " + this.toString() + " " + s);
    }
}