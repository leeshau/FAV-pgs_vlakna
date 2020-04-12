package pckg.workers;

/**takes text that includes chapter names and the paragraphs*/
public class BookMan extends AWorker {

    BookMan(AWorker upper, String filename, String text) {
        super(upper, filename, text);
    }

    @Override
    protected void process_text() {
        String[] chapters = this.text.split("CHAPTER [XIVL]+—[A-Z. —]+(\\r?\\n){3,}");
        Thread[] cms = new Thread[chapters.length];
        String[] cms_names = new String[chapters.length];
        this.thread_count = cms.length;
        for (int i = 0; i < chapters.length; i++) {
            ChapterMan cm = new ChapterMan(this, "" + (i + 1), chapters[i]);
            cms[i] = new Thread(cm);
            cms[i].start();
            cms_names[i] = cm.get_full_filename(false);
        }

        for (int i = 0; i < cms.length; i++) {
            try {
                cms[i].join();
                this.thread_count--;
                log(cms_names[i] + " OK");
            } catch (InterruptedException e) {
                log("could not join thread");
                e.printStackTrace();
            }
        }
    }
}