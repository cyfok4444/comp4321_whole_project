package database;
import org.rocksdb.*;
import org.rocksdb.RocksDBException;
import function.*;

import java.util.HashMap;

public class PageIDtoPageObject {


    /**
     *
     * @param dbpath
     */
    protected RocksDB rocksDB;
    protected Options options;
    protected  String dbpath;

    //Hashmap <PageID,ArrayList>
    public PageIDtoPageObject(String dbpath){

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

    /**
     * @return
     * @throws RocksDBException
     */
    /**
     * ??? what is the structure of the DB
     * @return
     * @throws RocksDBException
     */
    public HashMap<String,Long> getDateHashMapTable() throws RocksDBException{
        HashMap<String,Long>  hashMap = new HashMap<>();
        RocksIterator iterator = rocksDB.newIterator();

        for(iterator.seekToFirst(); iterator.isValid(); iterator.next()) {

            String key = new String(iterator.key());
            String value = new String(rocksDB.get(key.getBytes()));
            String[]s2 = value.split("JOHNMAVISOSCAR");
            String url = s2[1];
            Long date = Long.parseLong(s2[2]);
            hashMap.put(url,date);

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


    /**
     * get the Highest ID num in hashMap
     */


    /**
     * Should use after PageIDDB
     * @param key
     * @param pageObject
     * @return
     * @throws RocksDBException
     */
    public void addEntry (Integer key, PageObject pageObject) throws RocksDBException {
        System.out.println("Adding Key: " + key + " info: " + pageObject);
        rocksDB.put(key.toString().getBytes(), pageObject.getBytes());
    }


    public  String getEntry(Integer key){
        try {
            return new String(rocksDB.get(key.toString().getBytes()));
        }
        catch (RocksDBException e ){
            e.printStackTrace();
            return null;
        }

    }

    public static void main (String args[]){


    }
}
