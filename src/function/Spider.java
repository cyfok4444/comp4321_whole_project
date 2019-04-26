package function;

import database.*;
import org.htmlparser.util.ParserException;
import org.rocksdb.RocksDBException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Spider {
    public static void go(String url) throws RocksDBException {
        try {
            ArrayList<String> targetUrl = new ArrayList<>();
            targetUrl.add(url);
            HashMap<Integer,ArrayList<Integer>> parentPageId = new HashMap<>();                 //3.preparing parent link DB
            PageContentDBOperation pageContentDBOperation = new PageContentDBOperation(PathForDB.path);
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
                    HashMap<String,Integer> keyWordTf2 = ProcessString.keyWordTf(keywordsTf);

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
                    HashMap<String,Integer> titleTf2 = ProcessString.keyWordTf(titleTf);

                    HashMap<String,ArrayList<Integer>> titlePos2 = ProcessString.keyWordPos(titlePos);
                    ProcessString.stopWordRemovePos(keywordsPos2);

                    //1.staring DB Url to PageID
                    PageIDdBOperation pageIDdBOperation = new PageIDdBOperation(PathForDB.path);
                    pageIDdBOperation.addEntry(url2);
                    Integer pageId = pageIDdBOperation.getPageId(url2);

                    //2.staring DB child link
                    PageIDChildIDDB pageIDChildIDDB = new PageIDChildIDDB(PathForDB.path);
                    HashMap<Integer,ArrayList<Integer>> pageIdChildList = new HashMap<>();
                    ArrayList<Integer> childList = new ArrayList<>();
                    for( String s : childLinks ){
                        pageIDdBOperation.addEntry(s);
                        Integer childPageId = pageIDdBOperation.getPageId(s);
                        childList.add(childPageId);

                        //3.preparing parent link DB
                        if ( parentPageId.containsKey(childPageId)){
                            ArrayList<Integer> newParentLinks = parentPageId.get(childPageId);
                            newParentLinks.add(pageId);
                        }
                        else {
                            ArrayList<Integer> newParentLinks2 = new ArrayList<>();
                            parentPageId.put(childPageId,newParentLinks2);
                        }

                    }
                    pageIdChildList.put(pageId,childList);
                    pageIDChildIDDB.addEntry(pageIdChildList);

                    //4. Keyword to wordID
                    HashMap<Integer,ArrayList<Integer>> positionFileBody = new HashMap<>();
                    WordIDKeyword wordIDKeyword = new WordIDKeyword(PathForDB.path);
                    //5. Forwarded file for body
                    for( Map.Entry<String,ArrayList<Integer>> entry: keywordsPos2.entrySet()){
                        wordIDKeyword.addEntry(entry.getKey());
                        Integer thisWordId = wordIDKeyword.getWordId(entry.getKey());
                        positionFileBody.put(thisWordId,entry.getValue());
                    }
                    PageIDWordIDPosDBOperation pageIDWordIDPosDBOperation = new PageIDWordIDPosDBOperation(PathForDB.path);
                    pageIDWordIDPosDBOperation.addEntry(positionFileBody,pageId);

                    //5. forwarded file for title
                    HashMap<Integer,ArrayList<Integer>> positionFileTitle = new HashMap<>();

                    for( Map.Entry<String,ArrayList<Integer>> entry: titlePos2.entrySet()){
                        wordIDKeyword.addEntry(entry.getKey());
                        Integer thisWordId = wordIDKeyword.getWordId(entry.getKey());
                        positionFileTitle.put(thisWordId,entry.getValue());
                    }
                    PageIDWordIDPosDB2 pageIDWordIDPosDB2 = new PageIDWordIDPosDB2(PathForDB.path);
                    pageIDWordIDPosDB2.addEntry(positionFileTitle,pageId);

                    //6.Inverted file for body
                    WordIDPageIDDB wordIDPageIDDB = new WordIDPageIDDB(PathForDB.path);
                    for( Map.Entry<String,Integer> entry: titleTf2.entrySet()) {
                     //   wordIDKeyword.addEntry(entry.getKey());
                       // Integer thisWordId = wordIDKeyword.getWordId(entry.getKey());
                    }









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
