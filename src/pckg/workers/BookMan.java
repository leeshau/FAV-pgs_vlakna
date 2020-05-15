package pckg.workers;

import pckg.Res;

/**takes text that includes chapter names and the paragraphs*/
public class BookMan extends AWorker {

    BookMan(AWorker upper, String filename, String text) {
        super(upper, filename, text);
    }

    @Override
    protected void process_text() {
        String[] chapters = this.text.split("CHAPTER [XIVL]+—[A-Z. —]+(\\r?\\n){3,}");
        this.thread_count = chapters.length;
        int i = 0;
        for (String s : chapters) {
            Res.POOL_CMAN.submit(new ChapterMan(this, "" + ++i, s));
        }
    }
}