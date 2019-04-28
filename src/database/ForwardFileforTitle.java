package database;

import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;
import java.util.*;
import java.util.HashMap;

public class ForwardFileforTitle {
    protected RocksDB rocksDB;
    protected Options options;
    protected  String dbpath;
    protected HashMap<Integer,HashMap<Integer,ArrayList<Integer>>> hm = new HashMap<>();

    public ForwardFileforTitle(String dbpath){

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


    //PageID: KeywordID Pos
    public HashMap<Integer,HashMap<Integer,ArrayList<Integer>>> getHashMapTable() throws RocksDBException{

        HashMap<Integer,HashMap<Integer,ArrayList<Integer>>> hashMap = new HashMap<>();
        RocksIterator iterator = rocksDB.newIterator();
        for(iterator.seekToFirst(); iterator.isValid(); iterator.next()) {
            HashMap<Integer, ArrayList<Integer>> h2 = new HashMap<>();
            String key = new String(iterator.key());
            String value = new String(rocksDB.get(key.getBytes()));
            String [] single_wordID = value.split("Sep");
            for (int i = 0 ; i< single_wordID.length ; i++){
                String [] sep = single_wordID[i].split(" ");
                Integer wordId = Integer.parseInt(sep[0]);
                ArrayList<Integer> arr = new ArrayList<>();
                for (int j = 1 ; j < sep.length ; j++){
                    arr.add(Integer.parseInt(sep[j]));
                }
                h2.put(wordId,arr);
            }
            hashMap.put(Integer.parseInt(key),h2);

        }
        return hashMap;
    }
    public void setHashMapTable() throws RocksDBException{

        HashMap<Integer,HashMap<Integer,ArrayList<Integer>>> hashMap = new HashMap<>();
        RocksIterator iterator = rocksDB.newIterator();
        for(iterator.seekToFirst(); iterator.isValid(); iterator.next()) {
            HashMap<Integer, ArrayList<Integer>> h2 = new HashMap<>();
            String key = new String(iterator.key());
            String value = new String(rocksDB.get(key.getBytes()));
            String [] single_wordID = value.split("Sep");
            for (int i = 0 ; i< single_wordID.length ; i++){
                String [] sep = single_wordID[i].split(" ");
                Integer wordId = Integer.parseInt(sep[0]);
                ArrayList<Integer> arr = new ArrayList<>();
                for (int j = 1 ; j < sep.length ; j++){
                    arr.add(Integer.parseInt(sep[j]));
                }
                h2.put(wordId,arr);
            }
            hashMap.put(Integer.parseInt(key),h2);

        }
        hm = hashMap;
    }
    public boolean addEntry (HashMap<Integer,ArrayList<Integer>> hashMap , Integer key) throws RocksDBException {
        String s = "";
        for (Map.Entry<Integer, ArrayList<Integer>> item : hashMap.entrySet()) {
            Integer key1 = item.getKey();
            s+=Integer.toString(key1);
            s+=" ";
            ArrayList<Integer> value = item.getValue();
            for (int i = 0 ; i < value.size() ; i++){
                if (i != value.size()-1) {
                    s += value.get(i).toString();
                    s += " ";
                }
                else s += value.get(i).toString();

            }
            s+="Sep";
        }
        rocksDB.put(key.toString().getBytes(),s.getBytes());
        return true;
    }
    public boolean isEntryExists (Integer info){
        if (hm.containsKey(info)) return true;
        return false;
    }

    public static void main(String [] args) throws RocksDBException{  //done
        ForwardFileforTitle forwardFileforTitle = new ForwardFileforTitle("db/db_ForwardFileforTitle");
        HashMap<Integer,ArrayList<Integer> > hashMap = new HashMap<>();
        HashMap<Integer,ArrayList<Integer> > hashMap2 = new HashMap<>();
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(1);
        arrayList.add(2);
        arrayList.add(3);
        arrayList.add(4);
        arrayList.add(5);
        arrayList.add(6);
        arrayList.add(999);
        hashMap.put(100,arrayList);
        ArrayList<Integer> arrayList2 = new ArrayList<>();
        arrayList2.add(10);
        arrayList2.add(20);
        arrayList2.add(30);
        arrayList2.add(40);
        arrayList2.add(50);
        arrayList2.add(60);
        hashMap2.put(10000,arrayList2);
        forwardFileforTitle.addEntry(hashMap,10000);
        forwardFileforTitle.addEntry(hashMap2,100);

        RocksIterator iterator = forwardFileforTitle.rocksDB.newIterator();
        for(iterator.seekToFirst(); iterator.isValid(); iterator.next()){
            //System.out.println(new String(iterator.key()) + " " + new String(iterator.value()));

        }

        HashMap<Integer,HashMap<Integer,ArrayList<Integer>> > h = forwardFileforTitle.getHashMapTable();

        for (Map.Entry<Integer,HashMap<Integer,ArrayList<Integer>> > item : h.entrySet()) {
            Integer key = item.getKey();

            System.out.println(key);
            System.out.println(item.getValue());
        }
    }
}
