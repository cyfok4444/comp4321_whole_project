package database;

import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;
import java.util.*;
import java.util.HashMap;

public class PageIDWordIDPosDBOperation {
    protected RocksDB rocksDB;
    protected Options options;
    protected  String dbpath;

    public PageIDWordIDPosDBOperation (String dbpath){

        this.dbpath = dbpath;
        options = new Options();
        options.setCreateIfMissing(true);
        try {
            rocksDB = RocksDB.open(options,dbpath);
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
    public boolean addEntry (HashMap<Integer,ArrayList<Integer>> hashMap , String key) throws RocksDBException {
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
        rocksDB.put(key.getBytes(),s.getBytes());
        return true;
    }

    public static void main(String [] args) throws RocksDBException{
        PageIDWordIDPosDBOperation pageIDdBOperation = new PageIDWordIDPosDBOperation("/Users/tszmoonhung/IdeaProjects/comp4321_whole_project/function.PageIDKeyword");
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
        pageIDdBOperation.addEntry(hashMap,"10000");
        pageIDdBOperation.addEntry(hashMap,"100");

        RocksIterator iterator = pageIDdBOperation.rocksDB.newIterator();
        for(iterator.seekToFirst(); iterator.isValid(); iterator.next()){
            //System.out.println(new String(iterator.key()) + " " + new String(iterator.value()));

        }

        HashMap<Integer,HashMap<Integer,ArrayList<Integer>> > h = pageIDdBOperation.getHashMapTable();
        for (Map.Entry<Integer,HashMap<Integer,ArrayList<Integer>> > item : h.entrySet()) {
            Integer key = item.getKey();

            System.out.println(key);
            System.out.println(item.getValue());
        }
        /*
        String s = "";
        for (Map.Entry<Integer, ArrayList<Integer>> item : hashMap.entrySet()) {
            Integer key = item.getKey();
            s+=Integer.toString(key);
            s+=" ";
            ArrayList<Integer> value = item.getValue();
            for (int i = 0 ; i < value.size() ; i++){
                if (i != value.size()-1) {
                    s += value.get(i).toString();
                    s += " ";
                }
                else s += value.get(i).toString();

            }
            s+="Seperate";
        }
        System.out.println(s);
        String[] separte = s.split("Seperate");
        for (String k : separte)
        System.out.println(k);
        */
    }
}
