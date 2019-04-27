package function;
import javax.print.DocFlavor;
import java.net.URL;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;

public class Page {

    String title;
    String url ;
    Long lastModificationDate ;
    Integer size;
    HashMap<String,Integer> mostFreqKeywords = new HashMap<String,Integer>();
    ArrayList<Integer> childLinks = new ArrayList<Integer>();
    Integer maxTf;
    Integer maxTfTitle;
    Integer wordSize;


    public Integer getMaxTf() { return maxTf; }

    public String getTitle(){
        return title;
    }

    public String getURL(){
        return url;
    }

    public Integer getWordSize(){return wordSize;}

    public Integer getMaxTfTitle(){return maxTfTitle;}

    public ArrayList<Integer> getChildLinks(){
        return childLinks;
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

    public void addChildLinks(Integer pageID){
        childLinks.add(pageID);
    }

    public void setMaxTf(Integer maxTf) { this.maxTf=maxTf; }

    public void setWordSize(Integer wordSize){ this.wordSize=wordSize ;}

    public void setMaxTfTitle(Integer maxTfTitle) { this.maxTfTitle = maxTfTitle;}

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
        return (title+"JOHNMAVISOSCAR"+url+"JOHNMAVISOSCAR"+lastModificationDate.toString()+"JOHNMAVISOSCAR"+size.toString()+"JOHNMAVISOSCAR"+mostFreqKeywords.toString()+"JOHNMAVISOSCAR"+childLinks.toString()+"JOHNMAVISOSCAR"+maxTf.toString()+"JOHNMAVISOSCAR"+maxTfTitle.toString()+"JOHNMAVISOSCAR"+wordSize.toString()).getBytes();
    }
    public String toString(){
        return (title.toString()+url.toString()+lastModificationDate.toString()+size.toString()+mostFreqKeywords.toString()+childLinks.toString()+maxTf.toString()+maxTfTitle.toString()+wordSize.toString());
    }
    public static void main(String[] args){
        ;
    }
}
