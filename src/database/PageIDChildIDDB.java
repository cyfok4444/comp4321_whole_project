package database;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;
import java.util.ArrayList;
import java.util.*;



public class PageIDChildIDDB {
    protected RocksDB rocksDB;
    protected Options options;
    protected  String dbpath;

    public PageIDChildIDDB(String dbpath){

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

    public boolean addEntry(HashMap<Integer,ArrayList<Integer>> hashMap ) throws RocksDBException{
        System.out.println("Enter");
        for (Map.Entry<Integer, ArrayList<Integer>> item : hashMap.entrySet()){
            String key = item.getKey().toString();
            ArrayList<Integer> arr = item.getValue();
            String s = "";
            for (int i = 0 ; i< arr.size() ; i++){
                if (i == arr.size()-1)
                    s+=arr.get(i);
                else {
                    s += arr.get(i);
                    s+=" ";
                }
            }
            rocksDB.put(key.getBytes(),s.getBytes());
        }
        return true;
    }

    public static void main (String [] args) throws RocksDBException{
        PageIDChildIDDB pageIDChildIDDB = new PageIDChildIDDB("PageIDContent");
        HashMap<Integer,ArrayList<Integer>> hashMap = new HashMap<>();
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(1);
        arrayList.add(10);
        arrayList.add(1111);
        hashMap.put(123,arrayList);
        hashMap.put(321,arrayList);
        pageIDChildIDDB.addEntry(hashMap);
        HashMap<Integer,ArrayList<Integer>> RE = pageIDChildIDDB.getHashMapTable();
        for (Map.Entry<Integer, ArrayList<Integer>> item : RE.entrySet()){
            System.out.println(item.getKey());
            System.out.println(item.getValue());
        }


    }



}
