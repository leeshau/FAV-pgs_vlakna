package pckg.workers;

import pckg.Res;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

abstract class AWorker implements Runnable{

    HashMap<String, Integer> result_map;
    protected int thread_count = 0;
    protected boolean receive_text_running = false;
    protected static final int THREAD_LIMIT = 5;

    /**his boss*/
    AWorker upper;
    String filename;
    String text;

    /**every class implements its way of processing the text that was passed on it
     * including the result being written to result_map*/
    protected abstract void process_text();

    AWorker(AWorker upper, String filename, String text) {
        this.upper = upper;
        String classname = this.getClass().getSimpleName();
        this.filename = classname.substring(0, classname.length() - 3) + filename;
        this.text = text;
        this.result_map = new HashMap<>();
//        log(this.get_full_filename(false));
    }

    /**@return full file name of the new text file to be exported
     * true, returns whole filename with no spaces and separated with '/' symbols, the last element is missing on purpose
     * false, return the content to save, whole path separated with ' - ' symbols and Ok at the end*/
    protected String get_full_filename(boolean export, boolean state) {
        AWorker current_worker = this;
        LinkedList<String> names = new LinkedList<>();
        StringBuilder fullname = new StringBuilder();

        while(current_worker.upper != null){
            names.add(current_worker.filename);
            current_worker = current_worker.upper;
        }
        int names_size_fixed = names.size();
        if(export) {
            fullname.append(Res.BOOK_NAME);
            for (int i = names_size_fixed - 1; i >= 1; i--) {
                fullname.append(Res.SLASH);
                fullname.append(names.get(i).toLowerCase());
            }
            fullname.append(Res.SLASH + (state ? "state.txt" : names.get(0) + ".txt"));
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

    synchronized void receive_result(HashMap<String, Integer> map, AWorker aw) {
        this.receive_text_running = true;
//        log("receiving message from" + aw.toString());
        if(map == null){
            err_log(" receive_result unsuccessful");
            return;
        }
        synchronized (aw) {
            /*merging maps*/
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                String key = entry.getKey(); //TODO mozna pridej lowercase
                int count = result_map.getOrDefault(key, 0);
                result_map.put(key, count + entry.getValue());
            }
        }
//        log("message received from" + aw.toString());
        this.receive_text_running = false;
    }

    @Override
    public void run() {
        if(this.text.matches("((\\r)?\\n)+")) return;
//        log("started working");
        process_text();
        if(this.upper == null){
//            Res.print_map(this.result_map);
        }
        else {
            this.upper.receive_result(this.result_map, this);
        }
        print_state(null);
//        log("finished working");
    }

    /**prints files (path/state.txt) with OK statuses of analyzing*/
    void print_state(String state){
        String path = this.get_full_filename(true, true);
        state = state == null ? (this.get_full_filename(false, false) + " OK\n") : state;
        try {
            File f = new File(Res.EXPORT + path);
            f.getParentFile().mkdirs();
            if(!f.exists()) f.createNewFile();
            Files.write(Paths.get(Res.EXPORT + path), state.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(this.upper != null){
            this.upper.print_state(state);
        }
    }

    void log(String s){
        System.out.println("Log: " + this.toString() + " " + s);
    }
    private void err_log(String s){
        System.err.println("Log: " + this.toString() + " " + s);
    }
}