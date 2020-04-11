package pckg.workers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

abstract class AWorker implements Runnable{

    HashMap<String, Integer> result_map;

    /**his boss*/
    AWorker upper;
    String filename;
    String text;

    /**every class implements its way of processing the text that was passed on it
     * including the result being written to result_map*/
    protected abstract void process_text();

    public AWorker(AWorker upper, String filename, String text) {
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

    void send_result(HashMap<String, Integer> result_map) {
        if(this.upper == null || result_map == null){
            log("send_result unsuccessful, returning");
            return;
        }
        //TODO pokracuj, co se deje pri posilani hotovych dat vyse
    }

    void log(String s){
        System.out.println("Log: " + this.toString() + " " + s);
    }
}
