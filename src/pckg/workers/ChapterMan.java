package pckg.workers;

import pckg.Res;

/**takes chunks of text with possible double (or more) line breakers*/
public class ChapterMan extends AWorker {
    ChapterMan(AWorker upper, String filename, String text) {
        super(upper, filename, text);
    }

    @Override
    protected void process_text() {
        String[] paragraphs = this.text.split("(\\r?\\n){2,}");
        this.thread_count = paragraphs.length;
        int i = 0;
        for (String p : paragraphs) {
            try {
                Res.SEM_PMAN.acquire();
                new Thread(new ParagraphMan(this, "" + ++i, p)).start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Res.SEM_PMAN.release();
        }
    }
}
