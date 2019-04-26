import org.rocksdb.RocksDBException;
import java.io.*;
import java.util.ArrayList;
/*
public class TestProgram {
    public static void main(String[] args){
        //String path = "/Users/chunyinfok/Downloads/db";
        String path = "db";
        PageIndex.set(path);
        Page page = Spider.go("https://www.cse.ust.hk/");
         for ( int i = 0 ; i < 40 ; i ++ ){
            if ( page.url != page.childs.get(i) ) {
                Page page_child = Spider.go(page.childs.get(i));
            }
        }
        try{
            PageIndex.printAll();
        }
        catch (RocksDBException e ){
            e.printStackTrace();
        }
        try{
            FileWriter fw = new FileWriter("spider_result.txt");
            BufferedWriter bufw = new BufferedWriter(fw);
            try {
                PageIndex.writeTo(bufw);
            }
            catch ( RocksDBException e){
                e.printStackTrace();
            }
            bufw.flush();
            bufw.close();
        }
        catch (IOException e ){
            e.printStackTrace();
        }
    }
}
*/