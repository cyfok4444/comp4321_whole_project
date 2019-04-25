package database;
import org.rocksdb.*;
import org.rocksdb.RocksDBException;

import java.util.HashMap;

import java.util.*;
public class PageContentDBOperation extends DataBaseOperation{

    /**
     * Constructor of DataBaseoperation
     * Structure of the PageContent
     * tile
     * url
     * lastModificationDate
     * Size
     * @param dbpath
     */

    //Hashmap <PageID,ArrayList>
    public PageContentDBOperation(String dbpath){
        super(dbpath);
    }

    @Override
    public HashMap getHashMapTable() throws RocksDBException{
        HashMap<Integer,ArrayList<String>>  hashMap = new HashMap<>();
        RocksIterator iterator = rocksDB.newIterator();

        for(iterator.seekToFirst(); iterator.isValid(); iterator.next()) {

            String key = new String(iterator.key());
            String value = new String(rocksDB.get(key.getBytes()));
            String[]s2 = value.split("JOHNMAVISOSCAR");
            ArrayList<String> arrayList = new ArrayList<>();
            for ( int i = 0 ; i < s2.length ; i++ ){
                    arrayList.add(s2[i]);
                }
                hashMap.put(Integer.parseInt(key),arrayList);

        }
        return hashMap;
    }


    public void updateDB (String key, String info,HashMap<Integer,ArrayList<String>> hashMap) throws RocksDBException {
        rocksDB.remove(key.getBytes());
        for (String in : hashMap.get(Integer.parseInt(key))) {
            rocksDB.put(key.getBytes(),info.getBytes());
            rocksDB.put(key.getBytes(),"JOHNMAVISOSCAR".getBytes());
        }
    }


}
