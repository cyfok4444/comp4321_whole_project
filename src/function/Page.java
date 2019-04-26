package function;
import javax.print.DocFlavor;
import java.net.URL;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;

public class Page {

    String title;
    String url ;
    long lastModificationDate ;
    Integer size;
    HashMap<String,Integer> mostFreqKeywords = new HashMap<String,Integer>();
    ArrayList<Integer> parentLinks = new ArrayList<Integer>();
    Integer maxTf;

    public Integer getMaxTf() { return maxTf; }

    public String getTitle(){
        return title;
    }

    public String getURL(){
        return url;
    }

    public ArrayList<Integer> getParentLinks(){
        return parentLinks;
    }

    public long getLastModificationDate(){
        return lastModificationDate;
    }

    public HashMap<String,Integer> getKeywords(){
        return mostFreqKeywords;
    }

    public Integer getSize(){
        return size;
    }

    public void addParentLinks(Integer pageID){
        parentLinks.add(pageID);
    }

    public void setMaxTf(Integer maxTf) { this.maxTf=maxTf; }

    public void setSize(Integer size){
        this.size=size;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setUrl(String url){
        this.url = url;
    }


    public void setLastModificationDate(long lastModificationDate){
        this.lastModificationDate = lastModificationDate;
    }

    public void setMostFreqKeywords(HashMap<String,Integer> keywords){
        this.mostFreqKeywords=keywords;
    }

    public byte[] getBytes(){
        return (title+"JOHNMAVISOSCAR"+url+"JOHNMAVISOSCAR"+new Date(lastModificationDate).toString()+"JOHNMAVISOSCAR"+size.toString()+"JOHNMAVISOSCAR"+mostFreqKeywords.toString()+"JOHNMAVISOSCAR"+parentLinks.toString()).getBytes();
    }
    public String toString(){
        return (title.toString()+url.toString()+new Date(lastModificationDate).toString()+size.toString()+mostFreqKeywords.toString()+parentLinks.toString());
    }
    public static void main(String[] args){
        ;
    }
}
