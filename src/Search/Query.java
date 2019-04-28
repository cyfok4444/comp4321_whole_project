package Search;

import database.WordIDKeyword;
import function.ProcessString;
import org.rocksdb.RocksDBException;

import java.lang.reflect.Array;
import java.util.*;
public class Query {

     static private HashMap<String,Integer> word_ID;

    /**
     * constructor
     * Need to modified after having the database
     */

    public Query() {
        word_ID = new HashMap<>();
        word_ID.put("love",1);
        word_ID.put("hong",2);
        word_ID.put("kong",3);
        word_ID.put("ust",4);
        word_ID.put("cool",5);

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
     * @return 1 1 1 1 1 0 1 1 3 。。
     * @param query
     */

    /*public LinkedHashMap<Integer, ArrayList<Integer>> convertToWordIDPhrase (String query){
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
    }*/

    public ArrayList<Integer> convertToWordIDPhrase (String query){
        ArrayList<String> wordlist = new ArrayList<>();
        ArrayList<Integer> result = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(query);
        while (st.hasMoreTokens()) {
            wordlist.add(st.nextToken());
        }
        wordlist = ProcessString.removeRubbish(wordlist);
        ArrayList<String> process = ProcessString.doKeywordOnly(wordlist);
        for (int i = 0 ; i < process.size() ; i++)
            if (word_ID.containsKey(process.get(i))) result.add(word_ID.get(process.get(i)));
            else result.add(-1);
            return result;
    }

    public ArrayList<Integer> getDistinctSetOfKeyword (ArrayList<Integer> term){
        ArrayList<Integer> result = new ArrayList<>();
        for (Integer i : term){
            if (i == -1) continue;
            if (!result.contains(i)) result.add(i);
        }
        return result;
    }


    public boolean isPhraseSearch (String query){
        if ( query.charAt(0) == 34 && query.charAt(query.length()-1) == 34) return true;
        else return false;
    }

    public Double qLength (HashMap<Integer,Integer> query){
        Integer length = 0;
        for (Map.Entry<Integer,Integer>item : query.entrySet())
            length += item.getValue()*item.getValue();
        return Math.sqrt(length);
    }


    public static void main (String [] args) throws RocksDBException{
        Query query = new Query();
        query.word_ID = new HashMap<>();
        query.word_ID.put("love",1);
        query.word_ID.put("hong",2);
        query.word_ID.put("kong",3);
        query.word_ID.put("yeah",5);
        HashMap<Integer,Integer> h = query.convertToWordID("I Loves in Kong hong hong ");
        ArrayList<Integer> h2 = query.convertToWordIDPhrase("I I I Loves Kong in hong hong KOng Loving in YeAh at at");

        System.out.println(query.isPhraseSearch("\"hi\""));
        System.out.println(h2);
        //System.out.println(query.getDistinctSetOfKeyword(h2));


    }



}
