package pckg.workers;

import pckg.Res;

public class BookMan extends AWorker {

    public BookMan(AWorker upper, String filename, String text) {
        super(upper, filename, text);
    }

    @Override
    protected void process_text() {
        String[] chapters = this.text.split("CHAPTER [XIVL]+—[A-Z. —]+(\\r?\\n){3,}");
        Thread[] cms = new Thread[chapters.length];
        this.thread_count = cms.length;
        for (int i = 0; i < chapters.length; i++) {
            cms[i] = new Thread(new ChapterMan(this, "" + i, chapters[i]));
//            while(Res.thread_count_chapterman >= Res.LIMIT_CHAPTERMAN){
//                try {
//                    wait(Res.WAIT_MILIS);
//                } catch (InterruptedException e) {
//                    log("could not wait while creating thread for " + chapters[i]);
//                    e.printStackTrace();
//                }
//            }
//            Res.thread_count_chapterman++;
            cms[i].start();
        }

        for (Thread t : cms) {
            try {
                t.join();
                this.thread_count--;
            } catch (InterruptedException e) {
                log("could not join thread");
                e.printStackTrace();
            }
        }
    }
}