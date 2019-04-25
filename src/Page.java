import javax.print.DocFlavor;
import java.net.URL;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;

public class Page {

    String title;
    String url ;
    Date lastModificationDate ;
    Integer size;
    HashMap<String,Integer> keywords = new HashMap<String, Integer>();
    ArrayList <String> childs = new ArrayList<>();

    public String getTitle(){
        return title;
    }

    public String getURL(){
        return url;
    }

    public ArrayList<String> getChilds(){
        return childs;
    }

    public Date getLastModificationDate(){
        return lastModificationDate;
    }

    public HashMap<String,Integer> getKeywords(){
        return keywords;
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

    public void setURL(String url){
        this.url = url;
    }

    public void setChilds(ArrayList<String> childs){
        this.childs = childs;
    }

    public void setLastModificationDate(Date lastModificationDate){
        this.lastModificationDate = lastModificationDate;
    }

    public void setKeywords(HashMap<String,Integer> keywords){
        this.keywords=keywords;
    }

    public byte[] getBytes(){
        return (title+"JOHNMAVISOSCAR"+url+"JOHNMAVISOSCAR"+lastModificationDate.toString()+"JOHNMAVISOSCAR"+size.toString()+"JOHNMAVISOSCAR"+keywords.toString()+"JOHNMAVISOSCAR"+childs.toString()).getBytes();
    }
    public String toString(){
        return (title.toString()+url.toString()+lastModificationDate.toString()+size.toString()+keywords.toString()+childs.toString());
    }
    public static void main(String[] args){
        ;
    }
}
