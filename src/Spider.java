import org.htmlparser.util.ParserException;
import org.rocksdb.RocksDBException;

import java.util.ArrayList;
import java.util.Date;


public class Spider {
    public static Page go(String url){
        try {
            Page page = new Page();
            ArrayList<String> keywords = Crawler.extractWords(url);
            ProcessString.removeRubbish(keywords);
            ProcessString.stopWordRemove(keywords);
            page.setKeywords(ProcessString.keyWord(keywords));
            page.setChilds(Crawler.getChild(url));
            page.setLastModificationDate(Crawler.getLastModifiedDate
                    (url));
            page.setTitle(Crawler.getTitle(url));
            page.setURL(url);
            page.setSize(Crawler.getSize(url));

            try {
                PageIndex.addEntry(url, page);
            }
            catch (RocksDBException e)
            {
                e.printStackTrace();
            }
            finally {
                return page;
            }

        }
        catch (ParserException e){
            e.printStackTrace();
            return null;
        }

    }
}
