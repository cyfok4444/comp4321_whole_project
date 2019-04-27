package Search;

import function.ProcessString;

import java.util.*;
public class Query {

     HashMap<String,Integer> word_ID;

    /**
     * Only for non-phrase
     * @param query
     * @return <WordID, tf>
     */
    private HashMap<Integer,Integer> convertToWordID (String query){

        ArrayList<String> wordlist = new ArrayList<>();
        HashMap<Integer,Integer> idlist = new HashMap<>();
        StringTokenizer st = new StringTokenizer(query);
        while (st.hasMoreTokens()) {
            wordlist.add(st.nextToken());
        }

        wordlist = ProcessString.removeRubbish(wordlist);
        wordlist = ProcessString.stopWordRemoveTf(wordlist);
        ArrayList<String> k = new ArrayList<>();
        HashMap<String,Integer> keyWordTf = ProcessString.keyWordTf(wordlist);
        for (Map.Entry<String,Integer>item : keyWordTf.entrySet()){
           if (word_ID.containsKey(item.getKey())) idlist.put(word_ID.get(item.getKey()),item.getValue());

        }
        return idlist;

    }

    public static void main (String [] args){
        Query query = new Query();
        query.word_ID = new HashMap<>();
        query.word_ID.put("love",1);
        query.word_ID.put("hong",2);
        query.word_ID.put("kong",3);
        query.word_ID.put("yeah",5);
        HashMap<Integer,Integer> h = query.convertToWordID("I Loves Kong hong hong KOng Loving");
        System.out.println(h.toString());

    }

}
