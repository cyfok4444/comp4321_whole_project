package function;

import database.PageContentDBOperation;
import org.htmlparser.util.ParserException;
import org.rocksdb.RocksDBException;

import java.util.ArrayList;
import java.util.HashMap;


public class Spider {
    public static void go(String url) throws RocksDBException {
        try {
            ArrayList<String> targetUrl = new ArrayList<>();
            targetUrl.add(url);
            PageContentDBOperation pageContentDBOperation = new PageContentDBOperation("/Users/chunyinfok/Downloads/comp4321_pj/comp4321_whole_project/db");
            HashMap<String,Long> visistedList = pageContentDBOperation.getDateHashMapTable();
            while(!targetUrl.isEmpty()){
                String url2 = targetUrl.get(0);
                targetUrl.remove(0);
                if (visistedList.get(url2) == null){
                    Page page = new Page();
                    ArrayList<String> keywordsTf =  Crawler.extractWords(url2);
                    ProcessString.removeRubbish(keywordsTf);
                    ArrayList<String> keywordsPos = keywordsTf;
                    ProcessString.stopWordRemoveTf(keywordsTf);
                    ProcessString.keyWordTf(keywordsTf);

                    HashMap<String,ArrayList<Integer>> keywordsPos2 = ProcessString.keyWordPos(keywordsPos);
                    ProcessString.stopWordRemovePos(keywordsPos2);

                    Long date = Crawler.getLastModifiedDate(url2);
                    ArrayList<String> childLinks = Crawler.getChild(url2);
                    Integer size = Crawler.getSize(url2);

                    String title = Crawler.getTitle(url2);
                    String[] title2 = title.split(" ");
                    ArrayList<String> titleTf  = new ArrayList<>();
                    for ( String s : title2){
                        titleTf.add(s);
                    }
                    ProcessString.removeRubbish(titleTf);
                    ArrayList<String> titlePos = titleTf;
                    ProcessString.stopWordRemoveTf(titleTf);
                    ProcessString.keyWordTf(titleTf);

                    HashMap<String,ArrayList<Integer>> titlePos2 = ProcessString.keyWordPos(titlePos);
                    ProcessString.stopWordRemovePos(keywordsPos2);







                }
            }
            /*Page page = new Page();
            ArrayList<String> keywords = Crawler.extractWords(url);
            ProcessString.removeRubbish(keywords);

            ProcessString.stopWordRemove(keywords);
            // page.setMostFreqKeywords(function.ProcessString.keyWord(keywords));
            page.setLastModificationDate(Crawler.getLastModifiedDate
                    (url));
            page.setTitle(Crawler.getTitle(url));
            page.setUrl(url);
            page.setSize(Crawler.getSize(url));

            try {
                PageIndex.addEntry(url, page);
            }
            catch (RocksDBException e)
            {
                e.printStackTrace();
            }
            */

        }
        catch (ParserException e){
            e.printStackTrace();
        }

    }
}
