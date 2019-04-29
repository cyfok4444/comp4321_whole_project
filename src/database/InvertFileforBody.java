package database;

import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;

import java.util.HashMap;
import java.util.Map;
import java.nio.file.Paths;

public class InvertFileforBody {

    protected RocksDB rocksDB;
    protected Options options;
    protected  String dbpath;
    protected HashMap<Integer,HashMap<Integer,Integer>> hm = new HashMap<>();

    public InvertFileforBody(String dbpath){

        this.dbpath = dbpath;//Paths.get(dbpath).toAbsolutePath().normalize().toString();
        options = new Options();
        options.setCreateIfMissing(true);
        try {
            rocksDB = RocksDB.open(options,dbpath);
            hm = getHashMapTable();
        }
        catch (RocksDBException e){
            e.printStackTrace();
        }
    }


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
    public void setHashMapTable() throws RocksDBException{

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
        hm = hashMap;
    }
    public boolean addEntry (HashMap<Integer,Integer> hashMap , Integer key) throws RocksDBException {
        String s = "";
        for (Map.Entry<Integer, Integer> item : hashMap.entrySet()) {
            Integer key1 = item.getKey();
            Integer key2 = item.getValue();
            s+=Integer.toString(key1);
            s+=" ";
            s+=Integer.toString(key2);
            s+="Sep";
        }
        rocksDB.put(key.toString().getBytes(),s.getBytes());
        return true;
    }

    public HashMap<Integer,Integer> getEntry(Integer key){
        try{
            HashMap<Integer,Integer> hm = new HashMap();
            String value = new String (rocksDB.get(key.toString().getBytes()));
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

    public boolean isEntryExists (Integer info){
        if (hm.containsKey(info)) return true;
        return false;
    }

    public static void main (String args[]){ // done
        InvertFileforBody wp = new InvertFileforBody("db/db_InvertFileforBody");
     /*   HashMap<Integer,Integer> hm2 = new HashMap<>();
        hm2.put(10,10);
        hm2.put(1,999);*/
        try{
          //  wp.addEntry(hm2,10);
            HashMap<Integer, HashMap<Integer,Integer>> hm = wp.getHashMapTable();
            for (Map.Entry<Integer, HashMap<Integer,Integer>>Entry : hm.entrySet()){
                System.out.println(Entry.getKey().toString()+"    "+Entry.getValue().toString());
            }
        }
        catch ( RocksDBException e){
            e.printStackTrace();
        }
    }
}
