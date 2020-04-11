//package pckg.workers;
//
//import pckg.Utils;
//import pckg.WRComparer;
//import pckg.WordRecord;
//
//import java.io.BufferedReader;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.Iterator;
//import java.util.TreeSet;
//
//public class MasterBoss implements Runnable {
//    private TreeSet<WordRecord> wordRecords;
//    private final Object fileLock = new Object();
//    private BufferedReader in;
//    private int jokeID = 1;
//    private int thCount = 1;
//
//    MasterBoss (String file, int thCount) {
//        this.thCount = thCount;
//
//        System.out.println("Farmar - zacinam.");
//
//        // nacteni vstupniho souboru
//        System.out.println("Farmar - oteviram soubor...");
//
//        try {
//            FileReader fr = new FileReader(file);
//            in = new BufferedReader(fr);
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        // vytvoreni globalniho seznamu vysledku
//        wordRecords = new TreeSet<WordRecord>(new WRComparer());
//    }
//
//    public String getParagraph() {
//        synchronized (fileLock) {
//
//            String jokeText = "";
//            String line;
//
//            System.out.println("Zadam vtip.");
//
//            try {
//                while ((line = in.readLine()) != null) {
//                    if (line.trim().equals("%")) {
//                        System.out.println("Vtip nacten (" + jokeID + ").");
//                        jokeID++;
//                        return jokeText.trim();
//                    }
//                    jokeText += line + "\n";
//                }
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            System.out.println("Neni co cist.");
//            return "$$skonci$$";
//        }
//    }
//
//    // ukladani vysledku z jednoho vtipu do wordRecords
//    public void reportResult(TreeSet<WordRecord> results) {
//        synchronized (wordRecords) {
//            System.out.println("Pridavam vysledky.");
//
//            Iterator<WordRecord> it = results.iterator();
//
//            while (it.hasNext()) {
//                WordRecord listItem = it.next();
//
//                WordRecord wr = Utils.getWordRecord(listItem.word, wordRecords);
//
//                if (wr == null)
//                    wordRecords.add(listItem);
//                else {
//                    wordRecords.remove(wr);
//                    wr.frequency += listItem.frequency;
//                    wordRecords.add(wr);
//                }
//            }
//        }
//    }
//
//    // tisk vysledneho seznamu
//    public void printResult() {
//        Iterator<WordRecord> it = wordRecords.iterator();
//
//        System.out.println("Vysledek:");
//        System.out.println("=========");
//
//        while (it.hasNext()) {
//            WordRecord listItem = it.next();
//            System.out.println(listItem.word + " - " + listItem.frequency);
//        }
//    }
//
//    @Override
//    public void run() {
//        Thread[] workers = new Thread[thCount];
//        for (int i = 0; i < thCount; i++) {
//            workers[i] = new Thread(new ParagraphMan(this, "walker " + i));
//            workers[i].start();
//        }
//        for(Thread worker : workers) {
//            try {
//                worker.join();
//            } catch (InterruptedException e) {
//                System.err.println("Worker " + worker + " neocekavane skoncil.");
//            }
//        }
//    }
//}
