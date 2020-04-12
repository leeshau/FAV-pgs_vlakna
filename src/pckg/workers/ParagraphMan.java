package pckg.workers;

/**takes pure text with no double line breakers*/
public class ParagraphMan extends AWorker {

    ParagraphMan(AWorker chapterMan, String filename, String text) {
        super(chapterMan, filename, text);
    }

    @Override
    protected void process_text() {
        String[] text_arr = text.split("[,. \"”“;]+|(\\r?\\n)");
//        this.result_map.remove("\\r\\n");
        for (String word : text_arr){
            //avoid empty lines and lines with [ ] __ -- etc
            if(word.length() == 0 || !word.matches("[a-zA-Z]+|[0-9]+")) continue;
            String w = word.toLowerCase();
            int count = this.result_map.getOrDefault(w, 0);
            this.result_map.put(w, count + 1);
        }
    }
}