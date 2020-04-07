package pckg.workers;

import pckg.Utils;
import pckg.WRComparer;
import pckg.WordRecord;

import java.util.TreeSet;

public class ParagraphMan extends AWorker {

//    private final ChapterMan chapterMan;
//    private final String name;
//    private TreeSet<WordRecord> results;

    ParagraphMan(AWorker chapterMan, String filename, String text) {
        super(chapterMan, filename, text);
    }

    @Override
    public void run() {
        while(Res.thread_count >= Res.THREAD_LIMIT){
            try {
                wait(Res.WAIT_MILIS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log("started working");
        process_text(this.text);
        this.upper.send_result(this.result_map);
    }

    @Override
    protected void process_text(String text) {
        String[] text_arr = text.split("[,. \"”“]+");
        for (String word : text_arr){
            int count = this.result_map.getOrDefault(word, 0);
            this.result_map.put(word, count + 1);
        }
    }

//    public void compute() {
//
//        int i;
//
//        int jobCount = 0;
//        String par;
//
//        System.out.println(name + " - Zacinam makat.");
//
//        while ((par = chapterMan.getParagraph()).equals("$$skonci$$") == false) {
//            System.out.println(name + " - Dostal jsem praci.");
//
//            results = new TreeSet<WordRecord>(new WRComparer());
//
//            String[] words = par.split("[^A-Za-z0-9]+");
//
//            for (i = 0; i < words.length; i++) {
//
//                // prevedeni slova na mala pismena
//                words[i] = words[i].toLowerCase();
//
//                // zpracovani slova
//                WordRecord wr = Utils.getWordRecord(words[i], results);
//
//                if (wr == null) { // nove slovo ve vtipu
//                    wr = new WordRecord();
//                    wr.word = words[i];
//                    wr.frequency = 1;
//                }
//                else
//                { // slovo se jiz ve vtipu vyskytlo
//                    results.remove(wr);
//                    wr.frequency++;
//                }
//                results.add(wr);
//
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                System.out.println(name + " - Zpracovano slovo: " + " / " + wr.word + " /");
//            }
//            // ulozeni vysledku do globalniho seznamu u MasterBossa
//            chapterMan.reportResult(results);
//            jobCount++;
//        }
//        System.out.println(name + " - Koncim, zpracoval jsem " + jobCount + " vtipu.");
//
//        System.out.println(name + " - Tisknu vysledek.");
//        chapterMan.printResult();
//    }

}
