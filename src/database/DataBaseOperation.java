package database;

import org.rocksdb.RocksDB;
import org.rocksdb.Options;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;
import java.util.HashMap;


/**
 * For basic operation of the database
 */
public class DataBaseOperation {
    protected RocksDB rocksDB;
    protected Options options;
    protected  String dbpath;


    /**
     * constructor of the database
     * @param dbpath
     */
    public DataBaseOperation(String dbpath){
        this.dbpath = dbpath;
        options = new Options();
        try {
            rocksDB = RocksDB.open(options,dbpath);
        }
        catch (RocksDBException e){
            e.printStackTrace();
        }
    }


    /**
     *  Use for PageID WordID
     *  Assume all the data in the database has been checked for uniqueness
     *  Using this method to get the data from the database to HashMap for operation
     * @return
     * @throws RocksDBException
     */
    public HashMap getHashMapTable() throws RocksDBException{

        HashMap<Integer,String> hashMap = new HashMap<>();
        RocksIterator iterator = rocksDB.newIterator();


        for (iterator.seekToFirst(); iterator.isValid(); iterator.next()) {
            String key = new String(iterator.key());
            String value = new String(rocksDB.get(key.getBytes()));
            hashMap.put(Integer.parseInt(key),value);
        }

        return hashMap;
    }

    /**
     * if there is the same data in the database return false
     * else return true
     * @param hashMap is the hashmap the data we have in the db
     * @param info
     * @return
     */
    public boolean isEntryExists (HashMap<Integer,String> hashMap, String info){
        if (hashMap.containsValue(info)) return true;
        return false;
    }

    /**
     * get the Highest ID num in hashMap
     */
    public int getMaxId (HashMap<Integer,String> hashMap){
        int max = 0;
        for (Integer i : hashMap.keySet())
            if (max < i ) max = i;
        return max;
    }


    /**
     * basic operation of addEntry
     * will be override for some of the dbOperation subclass
     * @param hashMap
     * @param info
     * @return
     * @throws RocksDBException
     */
    public boolean addEntry (HashMap<Integer,String> hashMap, String info) throws RocksDBException{
        if (isEntryExists(hashMap,info)) return false;
        else{
            Integer max = getMaxId(hashMap);
            rocksDB.put(max.toString().getBytes(),info.getBytes());
            return true;
        }
    }
}
