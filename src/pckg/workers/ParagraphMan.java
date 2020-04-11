package pckg.workers;

public class ParagraphMan extends AWorker {

    ParagraphMan(AWorker chapterMan, String filename, String text) {
        super(chapterMan, filename, text);
    }

    @Override
    protected void process_text() {
        String[] text_arr = text.split("[,. \"”“;]+|(\\r?\\n)");
        for (String word : text_arr){
            String w = word.toLowerCase();
            int count = this.result_map.getOrDefault(w, 0);
            this.result_map.put(w, count + 1);
        }
    }
}
