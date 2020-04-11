package pckg.workers;

import java.util.ArrayList;
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
        this.filename = filename;
        this.text = text;
        this.result_map = new HashMap<>();
    }

    /**@return full file name of the new text file to be exported*/
    protected String get_full_filename() {
        AWorker current_worker = this;
        LinkedList<String> names = new LinkedList<>();
        StringBuilder fullname = new StringBuilder();

        while(current_worker.upper != null){
            names.add(current_worker.filename);
            current_worker = current_worker.upper;
        }
        int names_size_fixed = names.size();
        for (int i = 0; i < names_size_fixed; i++) {
            fullname.append("/");
            fullname.append(names.pop());
        }
        return fullname.toString();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " " + this.filename + ": ";
    }

    synchronized void receive_result(HashMap<String, Integer> map, AWorker aw) {
        this.receive_text_running = true;
        log("receiving message from" + aw.toString());
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

        /*finish the whole work*/
        if(this.thread_count == 0) {
            if (this.upper == null) {
                Res.print_map(this.result_map);
            } else {
                this.upper.receive_result(this.result_map, this);
            }
        }
        log("message received from" + aw.toString());
        this.receive_text_running = false;
    }

    void log(String s){
        System.out.println("Log: " + this.toString() + " " + s);
    }
    private void err_log(String s){
        System.err.println("Log: " + this.toString() + " " + s);
    }
}
