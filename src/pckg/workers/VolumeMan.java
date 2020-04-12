package pckg.workers;

/**takes text that begin with a book title (can begin with preface also)*/
public class VolumeMan extends AWorker {
    public VolumeMan(AWorker upper, String filename, String text) {
        super(upper, filename, text);
    }

    @Override
    protected void process_text() {
        String[] books = this.text.split("BOOK [A-Z]+—[A-Z ]+(\\r?\\n){3,}");
        Thread[] bms = new Thread[books.length];
        String[] bms_names = new String[books.length];
        this.thread_count = bms.length;
        for (int i = 0; i < books.length; i++) {
            BookMan bm = new BookMan(this, "" + (i + 1), books[i]);
            bms[i] = new Thread(bm);
            bms[i].start();
            bms_names[i] = bm.get_full_filename(false);
        }

        for (int i = 0; i < bms.length; i++) {
            try {
                bms[i].join();
                this.thread_count--;
                log(bms_names[i] + " OK");
            } catch (InterruptedException e) {
                log("could not join thread");
                e.printStackTrace();
            }
        }
    }
}
