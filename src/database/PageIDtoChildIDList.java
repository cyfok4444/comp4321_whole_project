package database;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;
import java.util.ArrayList;
import java.util.*;



public class PageIDtoChildIDList {
    protected RocksDB rocksDB;
    protected Options options;
    protected  String dbpath;
    protected  HashMap<Integer,ArrayList<Integer>> hm = new HashMap<>();
    public PageIDtoChildIDList(String dbpath){

        this.dbpath = dbpath;
        options = new Options();
        options.setCreateIfMissing(true);
        try {
            rocksDB = RocksDB.open(options,dbpath);
            setHashMapTable();
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
                arrayList.add(Integer.parseInt(s[i]));
            }
            hashMap.put(Integer.parseInt(key),arrayList);
        }
        return hashMap;
    }

    public void setHashMapTable() throws RocksDBException{
        HashMap<Integer,ArrayList<Integer>> hashMap = new HashMap<>();
        RocksIterator iterator = rocksDB.newIterator();
        for(iterator.seekToFirst(); iterator.isValid(); iterator.next()){
            String key = new String(iterator.key());
            String value = new String(rocksDB.get(key.getBytes()));
            String [] s = value.split(" ");
            ArrayList<Integer> arrayList = new ArrayList<>();
            for (int i = 0 ; i < s.length ; i++){
                arrayList.add(Integer.parseInt(s[i]));
            }
            hashMap.put(Integer.parseInt(key),arrayList);
        }
        hm = hashMap;
    }
    public boolean addEntry(Integer key,ArrayList<Integer> childs) throws RocksDBException{
        System.out.println("Enter");
            rocksDB.put(key.toString().getBytes(),childs.toString().getBytes());
        return true;
    }

    public boolean isEntryExists (Integer info){
        if (hm.containsKey(info)) return true;
        return false;
    }
    public static void main (String [] args) throws RocksDBException{
        PageIDtoChildIDList pageIDToChildIDList = new PageIDtoChildIDList("/Users/chunyinfok/Downloads/comp4321_pj/comp4321_whole_project/db");
       // HashMap<Integer,ArrayList<Integer>> hashMap = new HashMap<>();
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(10);
        arrayList.add(10);
        arrayList.add(1111);
        pageIDToChildIDList.addEntry(123,arrayList);
       /* HashMap<Integer,ArrayList<Integer>> RE = pageIDToChildIDList.getHashMapTable();
        for (Map.Entry<Integer, ArrayList<Integer>> item : RE.entrySet()){
            System.out.println(item.getKey());
            System.out.println(item.getValue());
        }*/
        System.out.println(new String(pageIDToChildIDList.rocksDB.get("123".getBytes())));



    }



}
