/* --
COMP4321 Lab1 Exercise
Student Name:
Student ID:
Section:
Email:
*/

import org.rocksdb.RocksDB;
import org.rocksdb.Options;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;

import javax.imageio.IIOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;


public class PageIndex
{
    private static RocksDB db ;
    private static Options options;
    private static String dbpath;
   /* PageIndex(String dbPath) throws RocksDBException
    {
        // the Options class contains a set of configurable DB options
        // that determines the behaviour of the database.
        this.options = new Options();
        this.options.setCreateIfMissing(true);

        // creat and open the database
        this.db = RocksDB.open(options, dbPath);
    }
*/
   public static void set(String path){
    dbpath = path;
    options = new Options();
    options.setCreateIfMissing(true);
    try {
        db = RocksDB.open(options, dbpath);
    }
    catch(RocksDBException e ){
        e.printStackTrace();
    }
   }

   public static String getEntry(String key){

       try {
           return new String(db.get(key.getBytes()));
       }
       catch (RocksDBException e ){
           e.printStackTrace();
           return null;
       }

   }
    public static void addEntry(String url, Page page) throws RocksDBException
    {
        // Add a "docX Y" entry for the key "word" into hashtable
        // ADD YOUR CODES HERE
        byte[] content = db.get(url.getBytes());
        if (content == null) {
            content = page.getBytes();
        }
        db.put(url.getBytes(), content);
    }
    public static void delEntry(String url) throws RocksDBException
    {
        // Delete the word and its list from the hashtable
        // ADD YOUR CODES HERE
        db.remove(url.getBytes());
    }
    public static void printAll() throws RocksDBException
    {
        // Print all the data in the hashtable
        // ADD YOUR CODES HERE
        RocksIterator iter = db.newIterator();
        int count = 0;
        for(iter.seekToFirst(); iter.isValid(); iter.next()) {
            String key = new String (iter.key());
            String s = PageIndex.getEntry(key);
            String[]s2 = s.split("JOHNMAVISOSCAR");
            for ( int i = 0 ; i < s2.length ; i++ ){
                if ( i  == s2.length-1 ){
                    String[] s3 = s2[i].split(", ");
                    for ( String l : s3 ){
                        System.out.println(l);
                    }
                }
                else {
                    System.out.println(s2[i]);
                }
            }
        }
    }


    public static void writeTo(BufferedWriter bufw) throws RocksDBException
    {
        // Print all the data in the hashtable
        // ADD YOUR CODES HERE
        RocksIterator iter = db.newIterator();

        for(iter.seekToFirst(); iter.isValid(); iter.next()) {
            String key = new String (iter.key());
            String s = PageIndex.getEntry(key);
            String[]s2 = s.split("JOHNMAVISOSCAR");
            for ( int i = 0 ; i < s2.length ; i++ ){
                if ( i  == s2.length-1 ){
                    String[] s3 = s2[i].split(", ");
                    for ( String l : s3 ){
                        try {
                            bufw.write(l);
                            bufw.newLine();

                        }
                        catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
                else {
                    try{
                        bufw.write(s2[i]);
                        bufw.newLine();
                    }
                    catch (IOException e ){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args)
    {
            // a static method that loads the RocksDB C++ library.
            RocksDB.loadLibrary();

            // modify the path to your database
            String path = "/Users/chunyinfok/Downloads/comp4321_project/db";



    }


}