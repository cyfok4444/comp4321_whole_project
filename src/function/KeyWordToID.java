package function;

import java.util.HashMap;

public class KeyWordToID {
    static int id = 0;
    static HashMap<String,Integer> wordToid = new HashMap<String, Integer>();
    public static void add(String keyword){

            if (wordToid.containsKey(keyword)) return;

            else {
                wordToid.put(keyword, id);
                id++;
            }
    }

    public static void remove(String keyword){

        if (wordToid.containsKey(keyword)) {
            wordToid.remove(keyword);
        }

        else {
            return;
        }
    }


}
