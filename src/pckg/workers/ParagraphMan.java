package pckg.workers;

/**takes pure text with no double line breakers*/
public class ParagraphMan extends AWorker {

    ParagraphMan(AWorker chapterMan, String filename, String text) {
        super(chapterMan, filename, text);
    }

    @Override
    protected void process_text() {
        String[] text_arr = text.split("[,. \"”“;]+|(\\r?\\n)");
        for (String word : text_arr){
            if(word.length() == 0 || !word.matches("[a-zA-Z]+|[0-9]+")) continue; //avoid empty lines and lines with [ ] __ -- etc
            String w = word.toLowerCase();
            //no need to use synchronized since the result_map here is dedicated only for this object
            int count = this.result_map.getOrDefault(w, 0);
            this.result_map.put(w, count + 1);
        }
        print_res();
        this.upper.receive_result(this.result_map);
        this.upper.dec_thread();
    }
}