package function;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import java.util.ArrayList;

import org.htmlparser.beans.StringBean;
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
            e.printStackTrace();
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
            e.printStackTrace();
        }

        return "";
    }


    public Integer getSize() throws ParserException{

        Parser parser = new Parser(url);
        int length = parser.getConnection().getContentLength();
        if (length == -1 ) {

            ArrayList<String> arr = extractWords(url);
            Integer i = 0;
            for (String s : arr) i += s.length();
            return i;
        }
        else return length;

    }

    public static Integer getSize(String url) throws ParserException{
            Parser parser = new Parser(url);
            int length = parser.getConnection().getContentLength();
            if (length == -1 ) {

                ArrayList<String> arr = extractWords(url);
                Integer i = 0;
                for (String s : arr) i += s.length();
                return i;
            }
            else return length;
    }

    public long getLastModifiedDate() throws ParserException{


            Parser parser = new Parser(url);
            long date = parser.getConnection().getLastModified();
            if (date == 0 )return System.currentTimeMillis();
            else return date;
    }

    public static long getLastModifiedDate(String url) throws ParserException{

            Parser parser = new Parser(url);
            long date = parser.getConnection().getLastModified();
            if (date == 0 )return System.currentTimeMillis();
            else return date;

    }



    public static void main (String[] args)
    {
        try
        {
            //Crawler crawler = new Crawler("https://stackoverflow.com/questions/5902306/in-java-its-possible-determine-the-size-of-a-web-page-before-download");
            Crawler crawler = new Crawler("https://www.cse.ust.hk");


            ArrayList<String> words = crawler.extractWords();

            ProcessString.removeRubbish(words);
            ProcessString.keyWordTf(words);
            ProcessString.stopWordRemoveTf(words);

            System.out.println("Words in "+crawler.url+":");
            for(int i = 0; i < words.size(); i++)
                System.out.print(words.get(i)+" ");
            System.out.println("\n\n");
            if (crawler.getLastModifiedDate() != 0) {
                System.out.println("Last: " + new Date(crawler.getLastModifiedDate()).toString() + "size: " + crawler.getSize());
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

