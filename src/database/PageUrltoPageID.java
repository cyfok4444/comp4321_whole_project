package database;

import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;

import java.util.HashMap;
import java.util.Map;
import java.nio.file.Paths;

public class PageUrltoPageID {

    protected RocksDB rocksDB;
    protected Options options;
    protected  String dbpath;
    public Integer availableID;
    public HashMap<String,Integer> hm = new HashMap<>();


        /**
         * constructor of the database
         * @param dbpath
         */
        public PageUrltoPageID(String dbpath){
            this.dbpath = dbpath;//Paths.get(dbpath).toAbsolutePath().normalize().toString();

            options = new Options();
            options.setCreateIfMissing(true);
            try {
                rocksDB = RocksDB.open(options,dbpath);
                setHashMapTable();
                availableID = getMaxId()+1;
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

            HashMap<String,Integer> hashMap = new HashMap<>();
            RocksIterator iterator = rocksDB.newIterator();


            for (iterator.seekToFirst(); iterator.isValid(); iterator.next()) {
                String key = new String(iterator.key());
                String value = new String(rocksDB.get(key.getBytes()));
                hashMap.put(key,Integer.parseInt(value));
            }

            return hashMap;
        }

    public void setHashMapTable() throws RocksDBException{

        HashMap<String,Integer> hashMap = new HashMap<>();
        RocksIterator iterator = rocksDB.newIterator();


        for (iterator.seekToFirst(); iterator.isValid(); iterator.next()) {
            String key = new String(iterator.key());
            String value = new String(rocksDB.get(key.getBytes()));
            hashMap.put(key,Integer.parseInt(value));
        }

        hm = hashMap;
    }



    /**
         * if there is the same data in the database return false
         * else return true
         * @param info
         * @return
         */
        public boolean isEntryExists (String info){
            if (hm.containsKey(info)) return true;
            return false;
        }

        /**
         * get the Highest ID num in hashMap
         */
        public int getMaxId (){
            int max = 0;
            for (String s : hm.keySet())
                if (max < hm.get(s) ) max = hm.get(s);
            return max;
        }

        public int getPageId(String url) throws RocksDBException {
            String PageId = new String(rocksDB.get(url.toString().getBytes()));

            return Integer.parseInt(PageId);
        }

        /**
         * basic operation of addEntry
         * will be override for some of the dbOperation subclass
         * @param
         * @param info
         * @return
         * @throws RocksDBException
         */
        public boolean addEntry (String info) throws RocksDBException{
            if (isEntryExists(info)) return false;
            else{
                System.out.println("Adding Key: " + availableID + " info: " + info);
                rocksDB.put(info.getBytes(),availableID.toString().getBytes());
                hm.put(info,availableID);
                availableID++;
                return true;
            }
        }

        public static void main(String [] args) throws RocksDBException{ // done
            PageUrltoPageID pageUrlToPageID = new PageUrltoPageID("db/db_PageUrlToPageID");
     
            HashMap<Integer,String> hm2 = new HashMap<>();
            for (Map.Entry<String,Integer>entry : pageUrlToPageID.hm.entrySet()){
                hm2.put(entry.getValue(),entry.getKey());
            }
            for (Map.Entry<Integer,String>entry:hm2.entrySet()){
                System.out.println(entry.getKey()+": "+entry.getValue());
            }

        }



}
