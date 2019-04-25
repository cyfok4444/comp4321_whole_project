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

        return hashMap;
    }

    public void updateDB (String key, String info) throws RocksDBException {
        rocksDB.merge(key.getBytes(), info.getBytes());
    }
}
