package database;

import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;
import java.util.ArrayList;
import java.util.HashMap;


public class PageIDChildIDDB {
    protected RocksDB rocksDB;
    protected Options options;
    protected  String dbpath;

    public PageIDChildIDDB(String dbpath){

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

    public HashMap<Integer,ArrayList<Integer>> getHashMapTable() throws RocksDBException{
        HashMap<Integer,ArrayList<Integer>> hashMap = new HashMap<>();
        RocksIterator iterator = rocksDB.newIterator();
        for(iterator.seekToFirst(); iterator.isValid(); iterator.next()){
            String key = new String(iterator.key());
            String value = new String(rocksDB.get(key.getBytes()));
            String [] s = value.split(" ");
            ArrayList<Integer> arrayList = new ArrayList<>();
            for (int i = 0 ; i < s.length ; i++){
                arrayList.add(Integer.parseInt(s.toString()));
            }
            hashMap.put(Integer.parseInt(key),arrayList);
        }
        return hashMap;
    }




}
