package database;

import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;
import java.util.*;
import java.util.HashMap;
import java.nio.file.Paths;
public class ForwardFileforBody {
    protected RocksDB rocksDB;
    protected Options options;
    protected  String dbpath;
    protected HashMap<Integer,HashMap<Integer,ArrayList<Integer>>> hm = new HashMap<>();

    public ForwardFileforBody(String dbpath){

        this.dbpath = dbpath; //Paths.get(dbpath).toAbsolutePath().normalize().toString();
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

    //every word need seperate How to seperate
    //PageID: KeywordID Pos
    public HashMap<Integer,HashMap<Integer,ArrayList<Integer>>> getHashMapTable() throws RocksDBException{

        HashMap<Integer,HashMap<Integer,ArrayList<Integer>>> hashMap = new HashMap<>();
        RocksIterator iterator = rocksDB.newIterator();
        for(iterator.seekToFirst(); iterator.isValid(); iterator.next()) {
            HashMap<Integer, ArrayList<Integer>> h2 = new HashMap<>();
            String key = new String(iterator.key());
            String value = new String(rocksDB.get(key.getBytes()));
            if ( value =="{}"){
                hashMap.put(Integer.parseInt(key),h2);
                continue;
            }
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
            if ( value =="{}"){
                hashMap.put(Integer.parseInt(key),h2);
                continue;
            }
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

    public static void main(String [] args) throws RocksDBException{ // done
        ForwardFileforBody forwardFileforBody = new ForwardFileforBody("db/db_ForwardFileforBody");
   /*     HashMap<Integer,ArrayList<Integer> > hashMap = new HashMap<>();
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(1);
        arrayList.add(2);
        arrayList.add(10);
        hashMap.put(1,arrayList);
        System.out.println("1.array:" + hashMap);
        forwardFileforBody.addEntry(hashMap,11);
        System.out.println("2:"+new String(forwardFileforBody.rocksDB.get("11".getBytes())));
        arrayList.add(100);
        hashMap.put(2,arrayList);
        System.out.println("3:"+hashMap);
        forwardFileforBody.addEntry(hashMap,11);
        forwardFileforBody.addEntry(hashMap,111111111);
        System.out.println("4:"+new String(forwardFileforBody.rocksDB.get("111111111".getBytes())));
        System.out.println(forwardFileforBody.hm);*/
        for (Map.Entry<Integer,HashMap<Integer,ArrayList<Integer>>> Entry: forwardFileforBody.hm.entrySet()){
            System.out.println((Entry.getKey().toString())+"         "+Entry.getValue().toString());
        }
        /*arrayList.add(1);
        hashMap.put(100,arrayList);
        ArrayList<Integer> arrayList2 = new ArrayList<>();
        arrayList2.add(1);
        arrayList2.add(2);
        arrayList2.add(3);
        arrayList2.add(4);
        arrayList2.add(5);
        arrayList2.add(6);
        hashMap.put(10000,arrayList2);
        pageIDdBOperation.addEntry(hashMap,10000);
        pageIDdBOperation.addEntry(hashMap,100);

        RocksIterator iterator = pageIDdBOperation.rocksDB.newIterator();
        for(iterator.seekToFirst(); iterator.isValid(); iterator.next()){
            //System.out.println(new String(iterator.key()) + " " + new String(iterator.value()));

        }

        HashMap<Integer,HashMap<Integer,ArrayList<Integer>> > h = pageIDdBOperation.getHashMapTable();
        for (Map.Entry<Integer,HashMap<Integer,ArrayList<Integer>> > item : h.entrySet()) {
            Integer key = item.getKey();

            System.out.println(key);
            System.out.println(item.getValue());
        }*/

    }
}
