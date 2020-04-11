package pckg.workers;

public class ChapterMan extends AWorker {

    public ChapterMan(AWorker upper, String filename, String text) {
        super(upper, filename, text);
    }

    @Override
    protected void process_text() {
        String[] paragraphs = this.text.split("(\\r?\\n){2,}");
        Thread[] pms = new Thread[paragraphs.length];
        this.thread_count = pms.length;
        for(int i = 0; i < paragraphs.length; i++){
//            if(paragraphs[i].length()==0) continue;
//            log(s);
            pms[i] = new Thread(new ParagraphMan(this, ""+i, paragraphs[i]));
            while(Res.thread_count_paragraphman >= Res.LIMIT_PARAGRAPHMAN){
                try {
                    wait(Res.WAIT_MILIS);
                } catch (InterruptedException e) {
                    log("could not wait while creating thread for " + paragraphs[i]);
                    e.printStackTrace();
                }
            }
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

    @Override
    public void run() {
//        while(Res.thread_count_chapterman >= Res.LIMIT_CHAPTERMAN){
//            try {
//                wait(Res.WAIT_MILIS);
//            } catch (InterruptedException e) {
//                log("could not wait");
//                e.printStackTrace();
//            }
//        }
        log("started working");

//        Res.thread_count_chapterman++;

        while(Res.process_text_chapterman_running){
            try {
                wait(Res.WAIT_MILIS);
            } catch (InterruptedException e) {
                log("could not wait");
                e.printStackTrace();
            }
        }
        process_text();
//        this.upper.send_result(this.result_map);
        Res.print_map(this.result_map);

//        Res.thread_count_chapterman--;
    }
}
