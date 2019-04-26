package database;
import org.rocksdb.*;
import org.rocksdb.RocksDBException;
import function.*;
import java.lang.reflect.Array;
import java.util.HashMap;

import java.util.*;
public class PageContentDBOperation{

    /**
     *
     * @param dbpath
     */
    protected RocksDB rocksDB;
    protected Options options;
    protected  String dbpath;

    //Hashmap <PageID,ArrayList>
    public PageContentDBOperation(String dbpath){

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
     * @param page
     * @return
     * @throws RocksDBException
     */
    public void addEntry (String key, Page page) throws RocksDBException {
        System.out.println("Adding Key: " + key + " info: " + page);
        rocksDB.put(key.getBytes(), page.getBytes());
    }


    public  String getEntry(String key){

        try {
            return new String(rocksDB.get(key.getBytes()));
        }
        catch (RocksDBException e ){
            e.printStackTrace();
            return null;
        }

    }

    public static void main (String args[]){
        ArrayList<String> x = new ArrayList<>();
        x.add("ZZZZ");
        x.add("AAAA");
        x.add("CCC");
        System.out.println(x.get(0));
        System.out.println("1"+x);
        x.remove(0);
        System.out.println(x.get(0));
        System.out.println("2"+x);



    }
}
