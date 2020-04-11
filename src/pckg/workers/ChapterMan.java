package pckg.workers;

public class ChapterMan extends AWorker {

    ChapterMan(AWorker upper, String filename, String text) {
        super(upper, filename, text);
    }

    @Override
    protected void process_text() {
        String[] paragraphs = this.text.split("(\\r?\\n){2,}");
        Thread[] pms = new Thread[paragraphs.length];
        this.thread_count = pms.length;
        for(int i = 0; i < paragraphs.length; i++){
            pms[i] = new Thread(new ParagraphMan(this, ""+i, paragraphs[i]));
            pms[i].start();
        }
        for(Thread t : pms){
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
