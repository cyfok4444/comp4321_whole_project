package database;
import org.rocksdb.*;
import org.rocksdb.RocksDBException;
import function.*;
import java.lang.reflect.Array;
import java.util.HashMap;

import java.util.*;
public class PageContentDBOperation{

    /**
     * Constructor of DataBaseoperation
     * Structure of the PageContent
     * PageID
     * tile
     * url
     * lastModificationDate
     * Size
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

    /*public HashMap getHashMapTable() throws RocksDBException{
        HashMap<Integer,ArrayList<String>>  hashMap = new HashMap<>();
        RocksIterator iterator = rocksDB.newIterator();

        for(iterator.seekToFirst(); iterator.isValid(); iterator.next()) {

            String key = new String(iterator.key());
            String value = new String(rocksDB.get(key.getBytes()));
            String[]s2 = value.split("JOHNMAVISOSCAR");
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.addAll(Arrays.asList(s2));

            hashMap.put(Integer.parseInt(key),arrayList);

        }
        return hashMap;
    }*/


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
        Page page = Spider.go("https://www.ust.hk/zh-hant/");
        System.out.println(page);
        PageContentDBOperation pc = new PageContentDBOperation("/Users/chunyinfok/Downloads/comp4321_pj/comp4321_whole_project/db");
        try{
            pc.rocksDB.put("1".getBytes(),page.getBytes());
            System.out.println(new String(pc.rocksDB.get("1".getBytes())));
        }
        catch ( RocksDBException e ){

        }
    }
}
