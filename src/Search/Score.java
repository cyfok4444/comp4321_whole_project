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

   private HashMap<String, Integer> pageIdTable;
   private HashMap<Integer,HashMap<Integer,ArrayList<Integer>>> titlePos;
   private HashMap<Integer,HashMap<Integer,ArrayList<Integer>>> contentPos;
   private HashMap<Integer, HashMap<Integer,Integer>> inverted_table_title;
   private HashMap<Integer, HashMap<Integer,Integer>> inverted_table_content;
   static private HashMap<Integer,Integer> maxtfContent;
   static private HashMap<Integer,Integer> maxtfTitle;
   private double N;
   private double N2;

    /**
     * Constructor
     */
    /**
     * Need to modified after having database
     */
    public Score(){
        pageIdTable = new HashMap<>();
        pageIdTable.put("page A",1);
        pageIdTable.put("page B",2);
        pageIdTable.put("page C",3);
        pageIdTable.put("page D",4);
        int a = pageIdTable.size();
        N = (double) a;
        contentPos = new HashMap<>();
        HashMap<Integer,ArrayList<Integer>> keyPos = new HashMap<>();
        ArrayList<Integer> pos_1 = new ArrayList<>();
        pos_1.add(1);
        ArrayList<Integer> pos_2 = new ArrayList<>();
        pos_2.add(4); pos_2.add(5);
        keyPos.put(1,pos_1); keyPos.put(2,pos_2);
        contentPos.put(1,keyPos);
        contentPos.put(2,keyPos);
        contentPos.put(3,keyPos);
        computeMaxTFContent();
        inverted_table_content = new HashMap<>();
        HashMap<Integer,Integer> word1 = new HashMap<>();
        HashMap<Integer,Integer> word2 = new HashMap<>();
        word2.put(1,10);
        word2.put(2,100);
        word1.put(1,3);
        word1.put(2,3);
        word1.put(3,3);
        inverted_table_content.put(1,word1);
        inverted_table_content.put(2,word2);
        inverted_table_content.put(3,word1);
        //System.out.println(inverted_table_content.toString());

    }
    /**
     * Dunno the size of the document a HashMapInput will be the best
     * Hard code of dsize
     * N = pageIDTable.size()
     * df = inverted_table.get(key).size()
     * idf = Math.log(N/df)/Math.log(2)
     * maxtf = max(pos.getkey().getkey.size()))
     * @param query
     * @return PageID Score
     */

   public HashMap<Integer,Double> calculateScoreContent (String query, double dsize) {

       Query query1 = new Query();
       HashMap<Integer,Double> result = new HashMap<>();
       HashMap<Integer,Integer> queryterm = query1.convertToWordID(query);
       //
       double qsize = query1.qLength(queryterm);

       for (Map.Entry<Integer,Integer> term : queryterm.entrySet()){
           HashMap<Integer,Integer> dochave = inverted_table_content.get(term.getKey());
           int f = dochave.size();
           double df = (double)f;
           for (Map.Entry<Integer,Integer> doc: dochave.entrySet()) {
               int t = doc.getValue();
               double tf = (double)t;

               int m = maxtfContent.get(doc.getKey());
               double maxtf = (double)m;
               double idf = Math.log(N/df)/Math.log(2);
               double dweight = (tf/maxtf) * idf;
               int qt = term.getValue();
               double qweight = (double)qt;
               if (!result.containsKey(doc.getKey()))
                    result.put(doc.getKey(),cos_sim(dweight,dsize,qweight,qsize));
               else
                    result.put(doc.getKey(),result.get(doc.getKey())+cos_sim(dweight,dsize,qweight,qsize));

           }
       }
       return result;

   }

    public HashMap<Integer,Double> calculateScoreCTitle (String query, double dsize){

        Query query1 = new Query();
        HashMap<Integer,Double> result = new HashMap<>();
        HashMap<Integer,Integer> queryterm = query1.convertToWordID(query);
        //
        double qsize = query1.qLength(queryterm);

        for (Map.Entry<Integer,Integer> term : queryterm.entrySet()){
            HashMap<Integer,Integer> dochave = inverted_table_title.get(term.getKey());
            int f = dochave.size();
            double df = (double)f;
            for (Map.Entry<Integer,Integer> doc: dochave.entrySet()) {
                int t = doc.getValue();
                double tf = (double)t;

                int m = maxtfTitle.get(doc.getKey());
                double maxtf = (double)m;
                double idf = Math.log(N2/df)/Math.log(2);
                double dweight = (tf/maxtf) * idf;
                int qt = term.getValue();
                double qweight = (double)qt;
                if (!result.containsKey(doc.getKey()))
                    result.put(doc.getKey(),cos_sim(dweight,dsize,qweight,qsize));
                else
                    result.put(doc.getKey(),result.get(doc.getKey())+cos_sim(dweight,dsize,qweight,qsize));

            }
        }
        return result;

    }


   public Double cos_sim (Double dweight, Double dsize, Double qweight , Double qsize){
       return (dweight*qweight)/(dsize*qsize);
   }


   public void computeMaxTFContent (){
       maxtfContent = new HashMap<>();
       for (Map.Entry<Integer,HashMap<Integer,ArrayList<Integer>>>item : contentPos.entrySet()){
           HashMap<Integer,ArrayList<Integer>> i = item.getValue();
           Integer max = 0;
           for (Map.Entry<Integer,ArrayList<Integer>> in : i.entrySet()){
               if (in.getValue().size()>max) max = in.getValue().size();
           }
           maxtfContent.put(item.getKey(),max);
       }

   }

    public void computeMaxTFTitle (){
        for (Map.Entry<Integer,HashMap<Integer,ArrayList<Integer>>>item : titlePos.entrySet()){
            HashMap<Integer,ArrayList<Integer>> i = item.getValue();
            Integer max = 0;
            for (Map.Entry<Integer,ArrayList<Integer>> in : i.entrySet()){
                if (in.getValue().size()>max) max = in.getValue().size();
            }
            maxtfContent.put(item.getKey(),max);
        }

    }

    /**
     * Compute the phrase search
     * @param query
     * @return
     */
    public HashMap<Integer,Double>  computePhraseContent (String query){
       Query query1 = new Query();
       LinkedHashMap<Integer, ArrayList<Integer>> phraseterm = query1.convertToWordIDPhrase(query);
       ArrayList<Integer> pageContainAllTerm = new ArrayList<>();
      for (Map.Entry<Integer, ArrayList<Integer>> doc: phraseterm.entrySet()){

      }
       return null;
    }

  public static void main (String [] a) {
       Score score = new Score();
       System.out.println(score.calculateScoreContent("loving love love love love hong hong hong loves",6));

  }

}
