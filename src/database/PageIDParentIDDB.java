package database;

import function.PathForDB;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;
import java.util.ArrayList;
import java.util.*;



public class PageIDParentIDDB {
    protected RocksDB rocksDB;
    protected Options options;
    protected  String dbpath;
    protected HashMap<Integer,ArrayList<Integer>> hm = new HashMap<>();

    public PageIDParentIDDB(String dbpath){

        this.dbpath = dbpath;
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

    public boolean addEntry(Integer key, ArrayList<Integer> parent) throws RocksDBException{
        System.out.println("Enter");
            rocksDB.put(key.toString().getBytes(),parent.toString().getBytes());
        return true;
    }

    public ArrayList<Integer> getEntry(Integer pageId) throws RocksDBException{
        String list = new String(rocksDB.get(pageId.toString().getBytes()));
        String [] list2 = list.split(" ");
        ArrayList<Integer> list3 = new ArrayList<>();
        for ( String s : list2){
            int i = Integer.parseInt(s);
            list3.add(i);
        }
        return list3;
    }

    public boolean isEntryExists (Integer info){
        if (hm.containsKey(info)) return true;
        return false;
    }



    public static void main (String [] args) throws RocksDBException{
        PageIDParentIDDB pageIDParentIDDB = new PageIDParentIDDB(PathForDB.path);
        HashMap<Integer,ArrayList<Integer>> hashMap = new HashMap<>();
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(1);
        arrayList.add(10);
        arrayList.add(1111);
        hashMap.put(123,arrayList);
        hashMap.put(321,arrayList);
        pageIDParentIDDB.addEntry(123,arrayList);
        HashMap<Integer,ArrayList<Integer>> RE = pageIDParentIDDB.getHashMapTable();
        for (Map.Entry<Integer, ArrayList<Integer>> item : RE.entrySet()){
            System.out.println(item.getKey());
            System.out.println(item.getValue());
        }
        System.out.println();


    }



}
