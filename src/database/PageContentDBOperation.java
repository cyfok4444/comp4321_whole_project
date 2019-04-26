package database;
import org.rocksdb.*;
import org.rocksdb.RocksDBException;

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

    public HashMap getHashMapTable() throws RocksDBException{
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
    }


    /**
     * if there is the same data in the database return false
     * else return true
     * @param hashMap is the hashmap the data we have in the db
     * @param info
     * @return
     */
    public boolean isEntryExists (HashMap<Integer, ArrayList<String>> hashMap, String info){
        if (hashMap.containsValue(info)) return true;
        return false;
    }

    /**
     * get the Highest ID num in hashMap
     */
    public int getMaxId (HashMap<Integer,ArrayList<String>> hashMap){
        int max = 0;
        for (Integer i : hashMap.keySet())
            if (max < i ) max = i;
        return max;
    }

    /**
     * Should use after PageIDDB
     * @param hashMap
     * @param info
     * @return
     * @throws RocksDBException
     */
    public boolean addEntry (HashMap<Integer,ArrayList<String>> hashMap, String info , String key) throws RocksDBException{
        if (isEntryExists(hashMap,info)) {
            //need to check particular info !!!!
            //if (change than update)
            updateDB(key,info,hashMap);
            return false;
        }
        else{
            Integer max = getMaxId(hashMap);
            rocksDB.put(max.toString().getBytes(),info.getBytes());
            return true;
        }
    }

    public void updateDB (String key, String info,HashMap<Integer,ArrayList<String>> hashMap) throws RocksDBException {
        rocksDB.remove(key.getBytes());
        for (String in : hashMap.get(Integer.parseInt(key))) {
            rocksDB.put(key.getBytes(),info.getBytes());
            rocksDB.put(key.getBytes(),"JOHNMAVISOSCAR".getBytes());
        }
    }


}
