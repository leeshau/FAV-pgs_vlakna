package pckg.workers;

import pckg.Res;

/**takes text that begin with a book title (can begin with preface also)*/
public class VolumeMan extends AWorker {
    public VolumeMan(AWorker upper, String filename, String text) {
        super(upper, filename, text);
    }

    @Override
    protected void process_text() {
        String[] books = this.text.split("BOOK [A-Z]+—[A-Z ]+(\\r?\\n){3,}");
        this.thread_count = books.length;
        int i = 0;
        for (String s : books) {
            Res.POOL_BMAN.submit(new BookMan(this, "" + ++i, s));
        }
    }
}
