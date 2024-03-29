package Search;

import database.WordtoWordID;
import function.ProcessString;
import org.rocksdb.RocksDBException;

import java.util.*;
public class Query {

     static private HashMap<String,Integer> word_ID ;
    /**
     * constructor
     * Need to modified after having the database
     */
     static {
         try {
             WordtoWordID wordtoWordID = new WordtoWordID("db/db_WordtoWordID");
             word_ID = wordtoWordID.getHashMapTable();
         }catch (RocksDBException e){
             e.printStackTrace();
         }

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
        System.out.println(wordlist);
        wordlist = ProcessString.keyWordTf(wordlist);
        System.out.println(wordlist);
        ArrayList<String> k = new ArrayList<>();
        HashMap<String,Integer> keyWordTf = ProcessString.stopWordRemoveTf(wordlist);
        System.out.println(keyWordTf);
        for (Map.Entry<String,Integer>item : keyWordTf.entrySet()){
           if (word_ID.containsKey(item.getKey())) {
               idlist.put(word_ID.get(item.getKey()), item.getValue());
           }

        }
        System.out.println(word_ID);
        return idlist;

    }

    /**
     * for phrase search
     * @return 1 1 1 1 -1 0 1 1 3 -1
     * denoted as the wordID of each of the query term
     * @param query
     */

    public ArrayList<Integer> convertToWordIDPhrase (String query){
        ArrayList<String> wordlist = new ArrayList<>();
        ArrayList<Integer> result = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(query);
        while (st.hasMoreTokens()) {
            wordlist.add(st.nextToken());
        }
        wordlist = ProcessString.removeRubbish(wordlist);
        ArrayList<String> process = ProcessString.doKeywordOnly(wordlist);
        for (int i = 0 ; i < process.size() ; i++) {
            if (word_ID.containsKey(process.get(i))) result.add(word_ID.get(process.get(i)));
            else result.add(-1);
        }
        System.out.println(result);
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


    public static boolean isPhraseSearch (String query){
        if ( query.charAt(0) == 34 && query.charAt(query.length()-1) == 34) {
            System.out.println(query + "true");
            return true;
        }
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
        System.out.println(query.qLength(query.convertToWordID("school")));
        //ArrayList<Integer> h2 = query.convertToWordIDPhrase("I I I Loves Kong in hong hong KOng Loving in YeAh at at");

        //System.out.println(query.isPhraseSearch("\"hi\""));
        //System.out.println(h);
        //System.out.println(query.getDistinctSetOfKeyword(h2));


    }



}
