package pckg.workers;

/**the root object, takes the whole text given to him*/
public class AllMan extends AWorker{
    public AllMan(AWorker upper, String filename, String text) {
        super(upper, filename, text);
    }

    @Override
    protected void process_text() {
        String[] volumes = this.text.split("(VOLUME [IVXL]+—[A-Z]+)");
        Thread[] vms = new Thread[volumes.length];
        for (int i = 0; i < volumes.length; i++) {
            vms[i] = new Thread(new VolumeMan(this, "" + i, volumes[i]));
            vms[i].start();
        }
        for (Thread bm : vms) {
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
