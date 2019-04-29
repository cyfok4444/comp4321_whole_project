package database;

import function.PathForDB;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;
import java.util.ArrayList;
import java.util.*;



public class PageIDtoParentIDList {
    protected RocksDB rocksDB;
    protected Options options;
    protected  String dbpath;
    protected HashMap<Integer,ArrayList<Integer>> hm = new HashMap<>();

    public PageIDtoParentIDList(String dbpath){

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
            value = value.substring(1,value.length()-1);
            String [] s = value.split(", ");
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
            value = value.substring(1,value.length()-1);
            String [] s = value.split(", ");
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
        list = list.substring(1,list.length()-1);
        String [] list2 = list.split(", ");
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



    public static void main (String [] args) throws RocksDBException{ // done
        PageIDtoParentIDList pageIDtoParentIDList = new PageIDtoParentIDList("db/db_PageIDtoParentIDList");
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(10);
        arrayList.add(10);
        arrayList.add(1111);
        pageIDtoParentIDList.addEntry(223,arrayList);

        System.out.println(pageIDtoParentIDList.hm);

        for (Map.Entry<Integer,ArrayList<Integer>> entry : pageIDtoParentIDList.getHashMapTable().entrySet()){
            System.out.println(entry.getKey().toString()+"     "+entry.getValue().toString());
        }


    }



}
