import org.htmlparser.Node;
import org.htmlparser.Parser;
import java.util.ArrayList;
import java.util.Vector;
import org.htmlparser.beans.StringBean;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TitleTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import java.util.StringTokenizer;
import org.htmlparser.beans.LinkBean;
import org.htmlparser.NodeFilter;
import java.util.*;


import java.net.URL;


public class Crawler
{
    private String url;
    Crawler(String _url)
    {
        url = _url;
    }
    public ArrayList<String> extractWords() throws ParserException

    {

        ArrayList<String> result = new ArrayList<>();
        StringBean bean = new StringBean();
        bean.setURL(url);
        bean.setLinks(false);
        String contents = bean.getStrings();
        StringTokenizer st = new StringTokenizer(contents);
        while (st.hasMoreTokens()) {
            result.add(st.nextToken());
        }
        return result;

    }

    public static ArrayList<String> extractWords(String url) throws ParserException

    {

        ArrayList<String> result = new ArrayList<>();
        StringBean bean = new StringBean();
        bean.setURL(url);
        bean.setLinks(false);
        String contents = bean.getStrings();
        StringTokenizer st = new StringTokenizer(contents);
        while (st.hasMoreTokens()) {
            result.add(st.nextToken());
        }
        return result;

    }

    public ArrayList<String> extractLinks() throws ParserException

    {
        // extract links in url and return them
        // ADD YOUR CODES HERE
        ArrayList<String> result = new ArrayList<>();
        LinkBean bean = new LinkBean();
        bean.setURL(url);
        URL[] urls = bean.getLinks();
        for (URL s : urls) {
            result.add(s.toString());
        }
        return result;

    }

    public static ArrayList<String> getChild(String url) throws ParserException{
        ArrayList<String> result = new ArrayList<>();
        LinkBean bean = new LinkBean();
        bean.setURL(url);
        URL[] urls = bean.getLinks();
        for (URL s : urls) {
            result.add(s.toString());
        }
        return result;
    }

    public String getTitle(){
        try {
            Parser parser = new Parser(url);
            NodeFilter titleNodeFilter = new NodeFilter() {
                @Override
                public boolean accept(Node node) {
                    return node instanceof TitleTag;
                }
            };
            NodeList nodeList = parser.extractAllNodesThatMatch(titleNodeFilter);
            return nodeList.asString();

        }
        catch (ParserException e){

        }

        return "";
    }

    public static String getTitle(String url){
        try {
            Parser parser = new Parser(url);
            NodeFilter titleNodeFilter = new NodeFilter() {
                @Override
                public boolean accept(Node node) {
                    return node instanceof TitleTag;
                }
            };
            NodeList nodeList = parser.extractAllNodesThatMatch(titleNodeFilter);
            return nodeList.asString();

        }
        catch (ParserException e){

        }

        return "";
    }


    public Integer getSize(){
        try {
            Parser parser = new Parser(url);
            int length = parser.getConnection().getContentLength();
            return length;

        }
        catch (ParserException e){

        }

        return -1;
    }

    public static Integer getSize(String url){
        try {
            Parser parser = new Parser(url);
            int length = parser.getConnection().getContentLength();
            return length;

        }
        catch (ParserException e){

        }

        return -1;
    }

    public Date getLastModifiedDate(){
        try {

            Parser parser = new Parser(url);
            long date = parser.getConnection().getLastModified();
            Date date1 = new Date(date);
            return date1;

        }
        catch (ParserException e){

        }

        return null;
    }

    public static Date getLastModifiedDate(String url){
        try {

            Parser parser = new Parser(url);
            long date = parser.getConnection().getLastModified();
            Date date1 = new Date(date);
            return date1;

        }
        catch (ParserException e){

        }

        return null;
    }



    public static void main (String[] args)
    {
        try
        {
            Crawler crawler = new Crawler("https://stackoverflow.com/questions/5902306/in-java-its-possible-determine-the-size-of-a-web-page-before-download");


            ArrayList<String> words = crawler.extractWords();

            ProcessString.removeRubbish(words);
            ProcessString.keyWord(words);
            ProcessString.stopWordRemove(words);

            System.out.println("Words in "+crawler.url+":");
            for(int i = 0; i < words.size(); i++)
                System.out.print(words.get(i)+" ");
            System.out.println("\n\n");
            //System.out.println(crawler.getTitle());
            //System.out.println("Size: " + crawler.getSize());
            if (crawler.getLastModifiedDate() != null) {
                System.out.println("Last: " + crawler.getLastModifiedDate().toString());
            }
            ArrayList<String> child = getChild("https://stackoverflow.com/questions/5902306/in-java-its-possible-determine-the-size-of-a-web-page-before-download");
            for (int i = 0 ; i < child.size() ; i++){
                System.out.println("Child: " + child.get(i));
            }

        }
        catch (ParserException e)
        {
            e.printStackTrace ();
        }

    }
}

