package function;
import javax.print.DocFlavor;
import java.net.URL;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;

public class PageObject {

    String title;
    String url ;
    Long lastModificationDate ;
    Integer size;
    HashMap<String,Integer> mostFreqKeywords = new HashMap<String,Integer>();




    public String getTitle(){
        return title;
    }

    public String getURL(){
        return url;
    }


    public Long getLastModificationDate(){
        return lastModificationDate;
    }

    public HashMap<String,Integer> getKeywords(){
        return mostFreqKeywords;
    }

    public Integer getSize(){
        return size;
    }


    public void setSize(Integer size){
        this.size=size;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setUrl(String url){
        this.url = url;
    }


    public void setLastModificationDate(Long lastModificationDate){
        this.lastModificationDate = lastModificationDate;
    }

    public void setMostFreqKeywords(HashMap<String,Integer> keywords){
        this.mostFreqKeywords=keywords;
    }

    public byte[] getBytes(){
        return (title+"JOHNMAVISOSCAR"+url+"JOHNMAVISOSCAR"+lastModificationDate.toString()+"JOHNMAVISOSCAR"+size.toString()+"JOHNMAVISOSCAR"+mostFreqKeywords.toString()).getBytes();
    }
    public String toString(){
        return (title.toString()+url.toString()+lastModificationDate.toString()+size.toString()+mostFreqKeywords.toString());
    }
    public static void main(String[] args){
        Double x = null;
        System.out.println((x==null));
    }
}
