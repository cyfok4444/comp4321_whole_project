package database;
import org.rocksdb.*;
import org.rocksdb.RocksDBException;
import function.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.Paths;

public class PageIDtoPageObject {


    /**
     *
     * @param dbpath
     */
    protected RocksDB rocksDB;
    protected Options options;
    protected  String dbpath;
    protected HashMap<Integer,PageObject> hm = new HashMap<>();

    //Hashmap <PageID,ArrayList>
    public PageIDtoPageObject(String dbpath){

        this.dbpath = dbpath;//Paths.get(dbpath).toAbsolutePath().normalize().toString();
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
            Long pageDate = Long.valueOf(s2[2]);
            hashMap.put(url,pageDate);

        }
        return hashMap;
    }

    public HashMap<Integer,PageObject> getHashMapTable() throws RocksDBException{
        HashMap<Integer,PageObject> hashMap = new HashMap<>();
        RocksIterator iterator = rocksDB.newIterator();
        for(iterator.seekToFirst(); iterator.isValid(); iterator.next()) {
            String key = new String(iterator.key());
            String value = new String(rocksDB.get(key.getBytes()));
            String [] single_wordID = value.split("JOHNMAVISOSCAR");
            String pageTitle = single_wordID[0];
            String pageUrl = single_wordID[1];
            Long pageDate = Long.valueOf(single_wordID[2]);
            Integer pageSize = Integer.parseInt(single_wordID[3]);
            HashMap<String,Integer> pageWords = new HashMap<>();
            String s1 = single_wordID[4];
            s1 = s1.substring(1,s1.length()-1);
            String[]s2 = s1.split(", ");
            for(String s3 :s2){
                String []s4 = s3.split("=");
                String name =s4[0];
                Integer number = Integer.parseInt(s4[1]);
                pageWords.put(name,number);
            }
            PageObject pageObject = new PageObject();
            pageObject.setUrl(pageUrl);
            pageObject.setTitle(pageTitle);
            pageObject.setSize(pageSize);
            pageObject.setMostFreqKeywords(pageWords);
            pageObject.setLastModificationDate(pageDate);
            hashMap.put(Integer.parseInt(key),pageObject);
        }
        return hashMap;
    }

    public void setHashMapTable() throws RocksDBException{
        HashMap<Integer,PageObject> hashMap = new HashMap<>();
        RocksIterator iterator = rocksDB.newIterator();
        for(iterator.seekToFirst(); iterator.isValid(); iterator.next()) {
            String key = new String(iterator.key());
            String value = new String(rocksDB.get(key.getBytes()));
            String [] single_wordID = value.split("JOHNMAVISOSCAR");
            String pageTitle = single_wordID[0];
            String pageUrl = single_wordID[1];
            Long pageDate = Long.valueOf(single_wordID[2]);
            Integer pageSize = Integer.parseInt(single_wordID[3]);
            HashMap<String,Integer> pageWords = new HashMap<>();
            String s1 = single_wordID[4];
            s1 = s1.substring(1,s1.length()-1);
            String[]s2 = s1.split(", ");
            for(String s3 :s2){
                String []s4 = s3.split("=");
                String name =s4[0];
                Integer number = Integer.parseInt(s4[1]);
                pageWords.put(name,number);
            }
            PageObject pageObject = new PageObject();
            pageObject.setUrl(pageUrl);
            pageObject.setTitle(pageTitle);
            pageObject.setSize(pageSize);
            pageObject.setMostFreqKeywords(pageWords);
            pageObject.setLastModificationDate(pageDate);
            hashMap.put(Integer.parseInt(key),pageObject);
        }
        hm = hashMap;
    }

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

    public boolean isEntryExists (Integer info){
        if (hm.containsKey(info)) return true;
        return false;
    }

    public static void main (String args[]) throws RocksDBException{
        PageIDtoPageObject pageIDtoPageObject = new PageIDtoPageObject("db/db_PageIDtoPageObject");
  /*      PageObject pageObject = new PageObject();
        Long date = new Long(10);
        pageObject.setLastModificationDate(date);
        HashMap<String,Integer> words = new HashMap<>();
        words.put("apple",10);
        words.put("orange",22);
        words.put("egg",35);
        words.put("AFWEQEQ",5);
        words.put("Ok",999);
        pageObject.setMostFreqKeywords(words);
        pageObject.setSize(500);
        pageObject.setTitle("GOOD title ar");
        pageObject.setUrl("!!!!!!!");
        pageIDtoPageObject.addEntry(999,pageObject);
        System.out.println(pageIDtoPageObject.getDateHashMapTable().get("!!!!!!!"));   */
        for (Map.Entry<Integer,PageObject> entry : pageIDtoPageObject.getHashMapTable().entrySet()){
            System.out.println(entry.getKey().toString()+"     "+entry.getValue().toString());
        }
        HashMap<Integer,PageObject> a = pageIDtoPageObject.getHashMapTable();
        System.out.println(a.get(132).toString());
    }
}
