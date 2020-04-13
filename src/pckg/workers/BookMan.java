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
        this.thread_count = cms.length;
        for (int i = 0; i < chapters.length; i++) {
            cms[i] = new Thread(new ChapterMan(this, "" + (i + 1), chapters[i]));
            cms[i].start();
        }

        for (Thread bm : cms) {
            try {
                bm.join();
                this.thread_count--;
            } catch (InterruptedException e) {
                log("could not join thread");
                e.printStackTrace();
            }
        }
    }
}