package Search;
import java.util.*;
/**
 * Calculation of the page score
 * 1) Get the String
 * 2) decide whether it is phrase search
 * 3) if not stopword remove and stemming query
 * 4) get the wordID of the query term
 * 5) match it to the inverted table one by one (TitleInverted and KeywordInverted)
 * 6) Calculate the partial score one by one
 *
 * Phrase Search
 * 1) Need to go to the inverted file first both (TitleInverted and KeywordInverted) (also need to calculate cos sim)
 * 2) If the first term is in the doc
 * 3) Using the PageId go to <PageID <Keyword Pos>>
 * 4) Then match the second term etc.
 * 5) The one who matches all the query get the highest bonus score  get term/query term
 * 6) ??? Dunno what should be the weighting of phrase search
 * 7) combine the page score and bonus
 *
 * 8) Get the page content according to the ranking
 * partial score
 *
 * Database that will use: PageContentDBOperation <PageID <PageContent>>
 *                         WordIDPageID <PageId <WordID tf>>
 *                         WordIDKeyword <Keyword WordID>
 *                         PageIDWordIDPos <PageID <WordID pos>>
 */

/// set . contains key.set
public class Score {
   private HashMap<String, Integer> word_wordID_table;
   private HashMap<Integer,ArrayList<Integer>> title_pos;
   private HashMap<Integer,ArrayList<Integer>> content_pos;
   private HashMap<Integer, HashMap<Integer,Integer>> inverted_table_title;
   private HashMap<Integer, HashMap<Integer,Integer>> inverted_table_content;

}
