package database;

import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;
import java.util.*;
import java.util.HashMap;

public class PageIDWordIDPosDB2 {
    protected RocksDB rocksDB;
    protected Options options;
    protected  String dbpath;
    protected HashMap<Integer,HashMap<Integer,ArrayList<Integer>>> hm = new HashMap<>();

    public PageIDWordIDPosDB2 (String dbpath){

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

    public static void main(String [] args) throws RocksDBException{
        PageIDWordIDPosDB2 pageIDWordIDPosDB2 = new PageIDWordIDPosDB2("/Users/tszmoonhung/IdeaProjects/comp4321_whole_project/function.PageIDKeyword");
        HashMap<Integer,ArrayList<Integer> > hashMap = new HashMap<>();
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(1);
        arrayList.add(2);
        arrayList.add(3);
        arrayList.add(4);
        arrayList.add(5);
        arrayList.add(6);
        hashMap.put(100,arrayList);
        ArrayList<Integer> arrayList2 = new ArrayList<>();
        arrayList2.add(1);
        arrayList2.add(2);
        arrayList2.add(3);
        arrayList2.add(4);
        arrayList2.add(5);
        arrayList2.add(6);
        hashMap.put(10000,arrayList2);
        pageIDWordIDPosDB2.addEntry(hashMap,10000);
        pageIDWordIDPosDB2.addEntry(hashMap,100);

        RocksIterator iterator = pageIDWordIDPosDB2.rocksDB.newIterator();
        for(iterator.seekToFirst(); iterator.isValid(); iterator.next()){
            //System.out.println(new String(iterator.key()) + " " + new String(iterator.value()));

        }

        HashMap<Integer,HashMap<Integer,ArrayList<Integer>> > h = pageIDWordIDPosDB2.getHashMapTable();
        for (Map.Entry<Integer,HashMap<Integer,ArrayList<Integer>> > item : h.entrySet()) {
            Integer key = item.getKey();

            System.out.println(key);
            System.out.println(item.getValue());
        }

    }
}
