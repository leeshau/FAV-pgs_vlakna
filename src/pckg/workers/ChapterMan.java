package pckg.workers;

/**takes chunks of text with possible double (or more) line breakers*/
public class ChapterMan extends AWorker {
    ChapterMan(AWorker upper, String filename, String text) {
        super(upper, filename, text);
    }

    @Override
    protected void process_text() {
        String[] paragraphs = this.text.split("(\\r?\\n){2,}");
        Thread[] pms = new Thread[paragraphs.length];
        String[] pms_names = new String[paragraphs.length];
        this.thread_count = pms.length;
        for(int i = 0; i < paragraphs.length; i++){
            ParagraphMan pm = new ParagraphMan(this, ""+(i + 1), paragraphs[i]);
            pms[i] = new Thread(pm);
            pms[i].start();
            pms_names[i] = pm.get_full_filename(false);
        }
        for (int i = 0; i < pms.length; i++) {
            try {
                pms[i].join();
                this.thread_count--;
                log(pms_names[i] + " OK");
            } catch (InterruptedException e) {
                log("could not join thread");
                e.printStackTrace();
            }
        }
    }
}
