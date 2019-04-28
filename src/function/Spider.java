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
            PageContentDBOperation pageContentDBOperation = new PageContentDBOperation(PathForDB.path);
            PageIDBodyInfoDB pageIDBodyInfoDB = new PageIDBodyInfoDB(PathForDB.path);
            PageIDChildIDDB pageIDChildIDDB = new PageIDChildIDDB(PathForDB.path);
            PageIDdBOperation pageIDdBOperation = new PageIDdBOperation(PathForDB.path);
            PageIDParentIDDB pageIDParentIDDB = new PageIDParentIDDB(PathForDB.path);
            PageIDTitleInfoDB pageIDTitleInfoDB = new PageIDTitleInfoDB(PathForDB.path);
            PageIDWordIDPosDB2 pageIDWordIDPosDB2 = new PageIDWordIDPosDB2(PathForDB.path);
            PageIDWordIDPosDBOperation pageIDWordIDPosDBOperation = new PageIDWordIDPosDBOperation(PathForDB.path);
            WordIDKeyword wordIDKeyword = new WordIDKeyword(PathForDB.path);
            WordIDPageIDDB wordIDPageIDDB = new WordIDPageIDDB(PathForDB.path);
            WordIDPageIDDB2 wordIDPageIDDB2 = new WordIDPageIDDB2(PathForDB.path);



            HashMap<String,Long> visistedList = pageContentDBOperation.getDateHashMapTable();
            while(!targetUrl.isEmpty()){
                String url2 = targetUrl.get(0);
                targetUrl.remove(0);
                Long lastDate = visistedList.get(url2);
                if ( lastDate != null &&  lastDate == Crawler.getLastModifiedDate(url2)){
                    continue;
                }
                else{
                    Page page = new Page();
                    page.setUrl(url2);
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

                    page.setMostFreqKeywords(mostFreqWords);

                    HashMap<String,ArrayList<Integer>> keywordsPos2 = ProcessString.keyWordPos(keywordsPos);
                    ProcessString.stopWordRemovePos(keywordsPos2);

                    Long date = Crawler.getLastModifiedDate(url2);
                    page.setLastModificationDate(date);
                    ArrayList<String> childLinks = Crawler.getChild(url2);
                    Integer size = Crawler.getSize(url2);
                    page.setSize(size);

                    String title = Crawler.getTitle(url2);
                    page.setTitle(title);
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
                    //PageIDdBOperation pageIDdBOperation = new PageIDdBOperation(PathForDB.path);
                    if ( lastDate==null) {
                        pageIDdBOperation.addEntry(url2);
                    }
                    Integer pageId = pageIDdBOperation.getPageId(url2);

                    //2.staring DB child link
                    //PageIDChildIDDB pageIDChildIDDB = new PageIDChildIDDB(PathForDB.path);
                    HashMap<Integer,ArrayList<Integer>> pageIdChildList = new HashMap<>();
                    ArrayList<Integer> childList = new ArrayList<>();
                    for( String s : childLinks ){
                        pageIDdBOperation.addEntry(s);
                        Integer childPageId = pageIDdBOperation.getPageId(s);
                        childList.add(childPageId);
                        // !!!!! add url to targetUrl !!!!
                        if ( !targetUrl.contains(s)){
                            targetUrl.add(s);
                        }
                    }
                    pageIDChildIDDB.addEntry(pageId,childList);

                    //3. Keyword to wordID
                    HashMap<Integer,ArrayList<Integer>> positionFileBody = new HashMap<>();
                    // WordIDKeyword wordIDKeyword = new WordIDKeyword(PathForDB.path);
                    //4. Forwarded file for body
                    for( Map.Entry<String,ArrayList<Integer>> entry: keywordsPos2.entrySet()){
                        wordIDKeyword.addEntry(entry.getKey());
                        Integer thisWordId = wordIDKeyword.getWordId(entry.getKey());
                        positionFileBody.put(thisWordId,entry.getValue());
                    }
                    //PageIDWordIDPosDBOperation pageIDWordIDPosDBOperation = new PageIDWordIDPosDBOperation(PathForDB.path);
                    pageIDWordIDPosDBOperation.addEntry(positionFileBody,pageId);

                    //5. forwarded file for title
                    HashMap<Integer,ArrayList<Integer>> positionFileTitle = new HashMap<>();

                    for( Map.Entry<String,ArrayList<Integer>> entry: titlePos2.entrySet()){
                        wordIDKeyword.addEntry(entry.getKey());
                        Integer thisWordId = wordIDKeyword.getWordId(entry.getKey());
                        positionFileTitle.put(thisWordId,entry.getValue());
                    }
                    //PageIDWordIDPosDB2 pageIDWordIDPosDB2 = new PageIDWordIDPosDB2(PathForDB.path);
                    pageIDWordIDPosDB2.addEntry(positionFileTitle,pageId);

                    //6.Inverted file for body
                    Double maxTf = 0.0;
                    Double bodySize = 0.0;
                    for( Map.Entry<String,Integer> entry: keyWordTf2.entrySet()) {
                        wordIDKeyword.addEntry(entry.getKey());
                        Integer wordID = wordIDKeyword.getWordId(entry.getKey());
                        Integer wordTf = entry.getValue();
                        bodySize += (double)wordTf*wordTf;
                        if ( wordTf > maxTf){
                            maxTf = (double)wordTf;
                        }
                        if (wordIDPageIDDB.isEntryExists(wordID)){
                            HashMap<Integer,Integer> hm = wordIDPageIDDB.getEntry(wordID);
                            hm.put(pageId,wordTf);
                            wordIDPageIDDB.addEntry(hm,wordID);
                        }
                        else{
                            HashMap<Integer,Integer> hm = new HashMap<>();
                            hm.put(pageId,wordTf);
                            wordIDPageIDDB.addEntry(hm,wordID);
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
                        wordIDKeyword.addEntry(entry.getKey());
                        Integer wordID = wordIDKeyword.getWordId(entry.getKey());
                        Integer wordTfTitle = entry.getValue();
                        titleSize += (double)wordTfTitle*wordTfTitle;
                        if ( wordTfTitle > maxTfTitle){
                            maxTfTitle = (double)wordTfTitle;
                        }
                        if (wordIDPageIDDB2.isEntryExists(wordID)){
                            HashMap<Integer,Integer> hm = wordIDPageIDDB.getEntry(wordID);
                            hm.put(pageId,wordTfTitle);
                            wordIDPageIDDB2.addEntry(hm,wordID);
                        }
                        else{
                            HashMap<Integer,Integer> hm = new HashMap<>();
                            hm.put(pageId,wordTfTitle);
                            wordIDPageIDDB2.addEntry(hm,wordID);
                        }
                    }
                    titleSize = Math.sqrt(titleSize);

                    ArrayList<Double> titleInfo = new ArrayList<>();
                    titleInfo.add(maxTfTitle);
                    titleInfo.add(titleSize);

                    //9,10. Title info and Body info
                    pageIDBodyInfoDB.addEntry(pageId,bodyInfo);
                    pageIDTitleInfoDB.addEntry(pageId,titleInfo);

                    //8. Add it to the parentDB of the child links
                    for ( String s : childLinks){
                        pageIDdBOperation.addEntry(s);
                        Integer childId = pageIDdBOperation.getPageId(s);
                        if(pageIDParentIDDB.isEntryExists(childId)){
                            ArrayList<Integer> parentList = pageIDParentIDDB.getEntry(childId);
                            if(!parentList.contains(pageId)) {
                                parentList.add(pageId);
                            }
                            pageIDParentIDDB.addEntry(childId,parentList);
                        }
                        else{
                            ArrayList<Integer> parentList = new ArrayList<>();
                            parentList.add(pageId);
                            pageIDParentIDDB.addEntry(childId,parentList);
                        }

                    }

                    pageContentDBOperation.addEntry(pageId,page);
                    visistedList = pageContentDBOperation.getDateHashMapTable(); // update the visited list;
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
