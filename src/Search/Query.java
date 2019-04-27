package Search;

import database.WordIDKeyword;
import function.ProcessString;
import org.rocksdb.RocksDBException;

import java.lang.reflect.Array;
import java.util.*;
public class Query {

     private HashMap<String,Integer> word_ID;

    /**
     * constructor
     */

    public Query(String db) throws RocksDBException {
        WordIDKeyword wordIDKeyword = new WordIDKeyword(db);
        word_ID = wordIDKeyword.getHashMapTable();
    }
    /**
     * Only for non-phrase
     * @param query
     * @return <WordID, tf>
     */
    public HashMap<Integer,Integer> convertToWordID (String query){

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

    /**
     * for phrase search
     * @return LinkedHashMap <WordId,Pos>
     * @param query
     */

    public LinkedHashMap<Integer, ArrayList<Integer>> convertToWordIDPhrase (String query){
        ArrayList<String> wordlist = new ArrayList<>();
        LinkedHashMap<Integer, ArrayList<Integer>> idlist = new LinkedHashMap<>();
        StringTokenizer st = new StringTokenizer(query);
        while (st.hasMoreTokens()) {
            wordlist.add(st.nextToken());
        }
        wordlist = ProcessString.removeRubbish(wordlist);
        HashMap<String,ArrayList<Integer>> word = ProcessString.keyWordPos(wordlist);
        for (Map.Entry<String,ArrayList<Integer>>item : word.entrySet()){
            if (word_ID.containsKey(item.getKey())) idlist.put(word_ID.get(item.getKey()),item.getValue());

        }
        return idlist;
    }

    public static void main (String [] args) throws RocksDBException{
        Query query = new Query("1");
        query.word_ID = new HashMap<>();
        query.word_ID.put("love",1);
        query.word_ID.put("hong",2);
        query.word_ID.put("kong",3);
        query.word_ID.put("yeah",5);
        HashMap<Integer,Integer> h = query.convertToWordID("I Loves Kong hong hong KOng Loving YeAh ");
        LinkedHashMap<Integer, ArrayList<Integer>> h2 = query.convertToWordIDPhrase("I Loves Kong hong hong KOng Loving YeAh ");

        System.out.println(h2.toString());

    }

}
