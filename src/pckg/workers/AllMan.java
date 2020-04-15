package pckg.workers;

import pckg.Res;

/**the root object, takes the whole text given to him*/
public class AllMan extends AWorker{
    public AllMan(AWorker upper, String filename, String text) {
        super(upper, filename, text);
        this.filename = filename;
    }

    @Override
    protected void process_text() {
        String[] volumes = this.text.split("(VOLUME [IVXL]+—[A-Z]+)");
        this.thread_count = volumes.length;
        int i = 0;
        for (String s : volumes) {
            try {
                Res.SEM_VMAN.acquire();
                new Thread(new VolumeMan(this, "" + i++, s)).start(); //Volumes start from 0 because of preface
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Res.SEM_VMAN.release();
        }
    }
}
