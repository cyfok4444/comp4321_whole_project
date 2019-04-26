package function;

import java.util.HashMap;

public class PageToID {
    static int id = 0;
    static HashMap<String,Integer> pageToId = new HashMap<String, Integer>();
    public static void add(String keyword){

        if (pageToId.containsKey(keyword)) return;

        else {
            pageToId.put(keyword, id);
            id++;
        }
    }

    public static void remove(String keyword){

        if (pageToId.containsKey(keyword)) {
            pageToId.remove(keyword);
        }

        else {
            return;
        }
    }


}
