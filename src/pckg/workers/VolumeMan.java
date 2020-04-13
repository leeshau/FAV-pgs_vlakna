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
        this.thread_count = bms.length;
        for (int i = 0; i < books.length; i++) {
            bms[i] = new Thread(new BookMan(this, "" + (i + 1), books[i]));
            bms[i].start();
        }

        for (Thread bm : bms) {
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
