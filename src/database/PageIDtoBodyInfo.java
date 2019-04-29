package database;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.*;


// PageID [maxtf,Size]

public class PageIDtoBodyInfo {
    protected RocksDB rocksDB;
    protected Options options;
    protected  String dbpath;
    protected  HashMap<Integer,ArrayList<Double>> hm = new HashMap<>();
    public PageIDtoBodyInfo(String dbpath){

        this.dbpath = dbpath;
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

    public HashMap<Integer,ArrayList<Double>> getHashMapTable() throws RocksDBException{
        HashMap<Integer,ArrayList<Double>> hashMap = new HashMap<>();
        RocksIterator iterator = rocksDB.newIterator();
        for(iterator.seekToFirst(); iterator.isValid(); iterator.next()){
            String key = new String(iterator.key());
            String value = new String(rocksDB.get(key.getBytes()));
            value = value.substring(1,value.length()-1);
            String [] s = value.split(", ");
            ArrayList<Double> arrayList = new ArrayList<>();
            for (int i = 0 ; i < s.length ; i++){
                arrayList.add(Double.parseDouble(s[i]));
            }
            hashMap.put(Integer.parseInt(key),arrayList);
        }
        return hashMap;
    }

    public void setHashMapTable() throws RocksDBException{
        HashMap<Integer,ArrayList<Double>> hashMap = new HashMap<>();
        RocksIterator iterator = rocksDB.newIterator();
        for(iterator.seekToFirst(); iterator.isValid(); iterator.next()){
            String key = new String(iterator.key());
            String value = new String(rocksDB.get(key.getBytes()));
            value = value.substring(1,value.length()-1);
            String [] s = value.split(", ");
            ArrayList<Double> arrayList = new ArrayList<>();
            for (int i = 0 ; i < s.length ; i++){
                arrayList.add(Double.parseDouble(s[i]));
            }
            hashMap.put(Integer.parseInt(key),arrayList);
        }
        hm = hashMap;
    }
    public boolean addEntry(Integer key,ArrayList<Double> info) throws RocksDBException{
        System.out.println("Enter");
        rocksDB.put(key.toString().getBytes(),info.toString().getBytes());
        return true;
    }

    public boolean isEntryExists (Integer key){
        if (hm.containsKey(key)) return true;
        return false;
    }
    public static void main (String [] args) throws RocksDBException{ // done
        PageIDtoBodyInfo pageIDtoBodyInfo = new PageIDtoBodyInfo("db/db_PageIDtoBodyInfo");
       // ArrayList<Double> arrayList = new ArrayList<>();
     //   arrayList.add(10.0);
       // arrayList.add(999.0);
       // pageIDtoBodyInfo.addEntry(1,arrayList);
        HashMap<Integer,ArrayList<Double>> hm = pageIDtoBodyInfo.getHashMapTable();
      //  System.out.println(hm);
        for (Map.Entry<Integer,ArrayList<Double>> entry : hm.entrySet()){
            System.out.println(entry.getKey().toString()+"     "+entry.getValue().toString());
        }
      //  System.out.println(pageIDtoBodyInfo.isEntryExists(1));
      //  System.out.println(pageIDtoBodyInfo.hm);
    }



}
