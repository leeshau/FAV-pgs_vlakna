package pckg;

import java.util.Iterator;
import java.util.TreeSet;

public class Utils {

    // ziskani zaznamu slova z results
    public static WordRecord getWordRecord (String item, TreeSet<WordRecord> results) {
        Iterator<WordRecord> it = results.iterator();

        while (it.hasNext()) {
            WordRecord listItem = it.next();

            if (listItem.word.equals(item))
                return listItem;
        }
        return null;
    }
}