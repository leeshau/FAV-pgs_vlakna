package pckg.workers;

public class ParagraphMan extends AWorker {

    public ParagraphMan(AWorker chapterMan, String filename, String text) {
        super(chapterMan, filename, text);
    }

    @Override
    public void run() {
        while(Res.thread_count >= Res.THREAD_LIMIT){ //TODO osetri pocitani vlaken
            try {
                wait(Res.WAIT_MILIS);
            } catch (InterruptedException e) {
                log("could not wait");
                e.printStackTrace();
            }
        }
        log("started working");
        Res.thread_count++;
        process_text();
//        this.upper.send_result(this.result_map);
        Res.print_map(this.result_map);
    }

    @Override
    protected void process_text() {
        while(Res.process_text_running){
            try {
                wait(Res.WAIT_MILIS);
            } catch (InterruptedException e) {
                log("could not wait");
                e.printStackTrace();
            }
        }
        Res.process_text_running = true;
        String[] text_arr = text.split("[,. \"”“]+");
        for (String word : text_arr){
            String w = word.toLowerCase();
            int count = this.result_map.getOrDefault(w, 0);
            this.result_map.put(w, count + 1);
        }
        Res.process_text_running = false;
    }

 /*   public void compute() {

        int i;

        int jobCount = 0;
        String par;

        System.out.println(name + " - Zacinam makat.");

        while ((par = chapterMan.getParagraph()).equals("$$skonci$$") == false) {
            System.out.println(name + " - Dostal jsem praci.");

            results = new TreeSet<WordRecord>(new WRComparer());

            String[] words = par.split("[^A-Za-z0-9]+");

            for (i = 0; i < words.length; i++) {

                // prevedeni slova na mala pismena
                words[i] = words[i].toLowerCase();

                // zpracovani slova
                WordRecord wr = Utils.getWordRecord(words[i], results);

                if (wr == null) { // nove slovo ve vtipu
                    wr = new WordRecord();
                    wr.word = words[i];
                    wr.frequency = 1;
                }
                else
                { // slovo se jiz ve vtipu vyskytlo
                    results.remove(wr);
                    wr.frequency++;
                }
                results.add(wr);

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(name + " - Zpracovano slovo: " + " / " + wr.word + " /");
            }
            // ulozeni vysledku do globalniho seznamu u MasterBossa
            chapterMan.reportResult(results);
            jobCount++;
        }
        System.out.println(name + " - Koncim, zpracoval jsem " + jobCount + " vtipu.");

        System.out.println(name + " - Tisknu vysledek.");
        chapterMan.printResult();
    } */

}
