package pckg.workers;

/**the root object, takes the whole text given to him*/
public class AllMan extends AWorker{
    public AllMan(AWorker upper, String filename, String text) {
        super(upper, filename, text);
    }

    @Override
    protected void process_text() {
//        String[] volumes = this.text.split("(VOLUME [IVXL]+—[A-Z]+)|(\\[THE END OF VOLUME [IVXL]+ [A-Z”“]+\\]([ a-zA-z]+)?)");
        String[] volumes = this.text.split("(VOLUME [IVXL]+—[A-Z]+)");
//        boolean b = volumes[0].matches("((\\r)?\\n)+");
        Thread[] vms = new Thread[volumes.length];
        String[] vms_names = new String[volumes.length];
        this.thread_count = vms.length;
        for (int i = 1; i < volumes.length; i++) {
            VolumeMan vm = new VolumeMan(this, "" + i, volumes[i]);
            vms[i] = new Thread(vm);
            vms[i].start();
            vms_names[i] = vm.get_full_filename(false);
        }

        for (int i = 1; i < vms.length; i++) {
            try {
                vms[i].join();
                this.thread_count--;
                log(vms_names[i] + " OK");
            } catch (InterruptedException e) {
                log("could not join thread");
                e.printStackTrace();
            }
        }
    }
}
