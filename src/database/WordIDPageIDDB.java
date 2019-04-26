package database;

import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WordIDPageIDDB {

    protected RocksDB rocksDB;
    protected Options options;
    protected  String dbpath;

    public WordIDPageIDDB (String dbpath){

        this.dbpath = dbpath;
        options = new Options();
        options.setCreateIfMissing(true);
        try {
            rocksDB = RocksDB.open(options,dbpath);
        }
        catch (RocksDBException e){
            e.printStackTrace();
        }
    }

    //every word need seperate How to seperate
    //PageID: KeywordID Pos
    public HashMap<Integer, HashMap<Integer,Integer>> getHashMapTable() throws RocksDBException{

        HashMap<Integer,HashMap<Integer,Integer>> hashMap = new HashMap<>();
        RocksIterator iterator = rocksDB.newIterator();
        for(iterator.seekToFirst(); iterator.isValid(); iterator.next()) {
            HashMap<Integer, Integer> h2 = new HashMap<>();
            String key = new String(iterator.key());
            String value = new String(rocksDB.get(key.getBytes()));
            String [] single_wordID = value.split("Sep");
            for (int i = 0 ; i< single_wordID.length ; i++){
                String [] sep = single_wordID[i].split(" ");
                Integer pageID = Integer.parseInt(sep[0]);
                Integer tf = Integer.parseInt(sep[1]);

                h2.put(pageID,tf);
            }
            hashMap.put(Integer.parseInt(key),h2);

        }
        return hashMap;
    }
    public boolean addEntry (HashMap<Integer,Integer> hashMap , String key) throws RocksDBException {
        String s = "";
        for (Map.Entry<Integer, Integer> item : hashMap.entrySet()) {
            Integer key1 = item.getKey();
            Integer key2 = item.getValue();
            s+=Integer.toString(key1);
            s+=" ";
            s+=Integer.toString(key2);
            s+="Sep";
        }
        rocksDB.put(key.getBytes(),s.getBytes());
        return true;
    }

    public HashMap<Integer,Integer> getEntry(String key){
        try{
            HashMap<Integer,Integer> hm = new HashMap();
            String value = new String (rocksDB.get(key.getBytes()));
            String [] single_wordID = value.split("Sep");
            for (int i = 0 ; i< single_wordID.length ; i++){
                String [] sep = single_wordID[i].split(" ");
                Integer pageID = Integer.parseInt(sep[0]);
                Integer tf = Integer.parseInt(sep[1]);

                hm.put(pageID,tf);
            }
            return hm;

        }
        catch ( RocksDBException e){
            e.printStackTrace();
        }
        return null;
    }

    public static void main (String args[]){
        WordIDPageIDDB wp = new WordIDPageIDDB("/Users/chunyinfok/Downloads/comp4321_pj/comp4321_whole_project/db");
        HashMap<Integer,Integer> hm2 = new HashMap<>();
        hm2.put(10,10);
        hm2.put(1,3);
        System.out.println(hm2);
        try{
            wp.addEntry(hm2,"5");
            String s = new String(wp.rocksDB.get("5".getBytes()));
            String s2 = wp.getEntry("5").toString();
            System.out.println(s);
            System.out.println(s2);
        }
        catch ( RocksDBException e){
            e.printStackTrace();
        }
    }
}
