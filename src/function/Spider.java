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

            //!!!!!opening the DB!!!!!!!
            ForwardFileforBody forwardFileforBody = new ForwardFileforBody("db/db_ForwardFileforBody");
            ForwardFileforTitle forwardFileforTitle = new ForwardFileforTitle("db/db_ForwardFileforTitle");
            InvertFileforBody invertFileforBody = new InvertFileforBody("db/db_InvertFileforBody");
            InvertFileforTitle invertFileforTitle = new InvertFileforTitle("db/db_InvertFileforTitle");
            PageIDtoBodyInfo pageIDtoBodyInfo = new PageIDtoBodyInfo("db/db_PageIDtoBodyInfo");
            PageIDtoChildIDList pageIDToChildIDList = new PageIDtoChildIDList("db/db_PageIDtoChildIDList");
            PageIDtoPageObject pageIDtoPageObject = new PageIDtoPageObject("db/db_PageIDtoPageObject");
            PageIDtoParentIDList pageIDtoParentIDList = new PageIDtoParentIDList("db/db_PageIDtoParentIDList");
            PageIDtoTitleInfo pageIDtoTitleInfo = new PageIDtoTitleInfo("db/db_PageIDtoTitleInfo");
            PageUrlToPageID pageUrlToPageID = new PageUrlToPageID("db/db_PageUrlToPageID");
            WordtoWordID wordtoWordID = new WordtoWordID("db/db_WordtoWordID");



            HashMap<String,Long> visistedList = pageIDtoPageObject.getDateHashMapTable();
            while(!targetUrl.isEmpty()){
                String url2 = targetUrl.get(0);
                targetUrl.remove(0);
                Long lastDate = visistedList.get(url2);
                if ( lastDate != null &&  lastDate == Crawler.getLastModifiedDate(url2)){
                    continue;
                }
                else{
                    PageObject pageObject = new PageObject();
                    pageObject.setUrl(url2);
                    ArrayList<String> keywordsTf =  Crawler.extractWords(url2);
                    ProcessString.removeRubbish(keywordsTf);
                    ArrayList<String> keywordsPos = keywordsTf;
                    ProcessString.stopWordRemoveTf(keywordsTf);
                    HashMap<String,Integer> keyWordTf2 = ProcessString.keyWordTf(keywordsTf);

                    HashMap<String,Integer> mostFreqWords = new HashMap<>();
                    for ( int i = 0 ; i < 5 ; i++){
                        String freqWordString = "";
                        Integer freqWordTf = 0;
                        for ( Map.Entry<String,Integer> Entry: keyWordTf2.entrySet()){
                            if ( Entry.getValue() > freqWordTf){
                                if ( mostFreqWords.containsKey(Entry.getKey()))
                                {
                                    continue;
                                }
                                freqWordString = Entry.getKey();
                                freqWordTf = Entry.getValue();
                            }
                        }
                        mostFreqWords.put(freqWordString,freqWordTf);
                    }

                    pageObject.setMostFreqKeywords(mostFreqWords);

                    HashMap<String,ArrayList<Integer>> keywordsPos2 = ProcessString.keyWordPos(keywordsPos);
                    ProcessString.stopWordRemovePos(keywordsPos2);

                    Long date = Crawler.getLastModifiedDate(url2);
                    pageObject.setLastModificationDate(date);
                    ArrayList<String> childLinks = Crawler.getChild(url2);
                    Integer size = Crawler.getSize(url2);
                    pageObject.setSize(size);

                    String title = Crawler.getTitle(url2);
                    pageObject.setTitle(title);
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

                    //1.starting DB Url to PageID
                    //PageUrlToPageID pageUrlToPageID = new PageUrlToPageID(PathForDB.path);
                    if ( lastDate==null) {
                        pageUrlToPageID.addEntry(url2);
                    }
                    Integer pageId = pageUrlToPageID.getPageId(url2);

                    //2.staring DB child link
                    //PageIDtoChildIDList pageIDToChildIDList = new PageIDtoChildIDList(PathForDB.path);
                    HashMap<Integer,ArrayList<Integer>> pageIdChildList = new HashMap<>();
                    ArrayList<Integer> childList = new ArrayList<>();
                    for( String s : childLinks ){
                        pageUrlToPageID.addEntry(s);
                        Integer childPageId = pageUrlToPageID.getPageId(s);
                        childList.add(childPageId);
                        // !!!!! add url to targetUrl !!!!
                        if ( !targetUrl.contains(s)){
                            targetUrl.add(s);
                        }
                    }
                    pageIDToChildIDList.addEntry(pageId,childList);

                    //3. Keyword to wordID
                    HashMap<Integer,ArrayList<Integer>> positionFileBody = new HashMap<>();
                    // WordtoWordID wordtoWordID = new WordtoWordID(PathForDB.path);
                    //4. Forwarded file for body
                    for( Map.Entry<String,ArrayList<Integer>> entry: keywordsPos2.entrySet()){
                        wordtoWordID.addEntry(entry.getKey());
                        Integer thisWordId = wordtoWordID.getWordId(entry.getKey());
                        positionFileBody.put(thisWordId,entry.getValue());
                    }
                    //ForwardFileforBody forwardFileforBody = new ForwardFileforBody(PathForDB.path);
                    forwardFileforBody.addEntry(positionFileBody,pageId);

                    //5. forwarded file for title
                    HashMap<Integer,ArrayList<Integer>> positionFileTitle = new HashMap<>();

                    for( Map.Entry<String,ArrayList<Integer>> entry: titlePos2.entrySet()){
                        wordtoWordID.addEntry(entry.getKey());
                        Integer thisWordId = wordtoWordID.getWordId(entry.getKey());
                        positionFileTitle.put(thisWordId,entry.getValue());
                    }
                    //ForwardFileforTitle forwardFileforTitle = new ForwardFileforTitle(PathForDB.path);
                    forwardFileforTitle.addEntry(positionFileTitle,pageId);

                    //6.Inverted file for body
                    Double maxTf = 0.0;
                    Double bodySize = 0.0;
                    for( Map.Entry<String,Integer> entry: keyWordTf2.entrySet()) {
                        wordtoWordID.addEntry(entry.getKey());
                        Integer wordID = wordtoWordID.getWordId(entry.getKey());
                        Integer wordTf = entry.getValue();
                        bodySize += (double)wordTf*wordTf;
                        if ( wordTf > maxTf){
                            maxTf = (double)wordTf;
                        }
                        if (invertFileforBody.isEntryExists(wordID)){
                            HashMap<Integer,Integer> hm = invertFileforBody.getEntry(wordID);
                            hm.put(pageId,wordTf);
                            invertFileforBody.addEntry(hm,wordID);
                        }
                        else{
                            HashMap<Integer,Integer> hm = new HashMap<>();
                            hm.put(pageId,wordTf);
                            invertFileforBody.addEntry(hm,wordID);
                        }
                    }
                    bodySize = Math.sqrt(bodySize);

                    ArrayList<Double> bodyInfo = new ArrayList<>();
                    bodyInfo.add(maxTf);
                    bodyInfo.add(bodySize);

                    //7.Inverted file for title
                    Double maxTfTitle = 0.0;
                    Double titleSize = 0.0;
                    for( Map.Entry<String,Integer> entry: titleTf2.entrySet()) {
                        wordtoWordID.addEntry(entry.getKey());
                        Integer wordID = wordtoWordID.getWordId(entry.getKey());
                        Integer wordTfTitle = entry.getValue();
                        titleSize += (double)wordTfTitle*wordTfTitle;
                        if ( wordTfTitle > maxTfTitle){
                            maxTfTitle = (double)wordTfTitle;
                        }
                        if (invertFileforTitle.isEntryExists(wordID)){
                            HashMap<Integer,Integer> hm = invertFileforBody.getEntry(wordID);
                            hm.put(pageId,wordTfTitle);
                            invertFileforTitle.addEntry(hm,wordID);
                        }
                        else{
                            HashMap<Integer,Integer> hm = new HashMap<>();
                            hm.put(pageId,wordTfTitle);
                            invertFileforTitle.addEntry(hm,wordID);
                        }
                    }
                    titleSize = Math.sqrt(titleSize);

                    ArrayList<Double> titleInfo = new ArrayList<>();
                    titleInfo.add(maxTfTitle);
                    titleInfo.add(titleSize);

                    //9,10. Title info and Body info
                    pageIDtoBodyInfo.addEntry(pageId,bodyInfo);
                    pageIDtoTitleInfo.addEntry(pageId,titleInfo);

                    //8. Add it to the parentDB of the child links
                    for ( String s : childLinks){
                        pageUrlToPageID.addEntry(s);
                        Integer childId = pageUrlToPageID.getPageId(s);
                        if(pageIDtoParentIDList.isEntryExists(childId)){
                            ArrayList<Integer> parentList = pageIDtoParentIDList.getEntry(childId);
                            if(!parentList.contains(pageId)) {
                                parentList.add(pageId);
                            }
                            pageIDtoParentIDList.addEntry(childId,parentList);
                        }
                        else{
                            ArrayList<Integer> parentList = new ArrayList<>();
                            parentList.add(pageId);
                            pageIDtoParentIDList.addEntry(childId,parentList);
                        }

                    }

                    pageIDtoPageObject.addEntry(pageId, pageObject);
                    visistedList = pageIDtoPageObject.getDateHashMapTable(); // update the visited list;
                }
            }
            /*PageObject page = new PageObject();
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
