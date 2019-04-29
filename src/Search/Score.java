package Search;

import database.*;
import org.rocksdb.RocksDBException;

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
 * Database that will use: PageIDtoPageObject <PageID <PageContent>>
 *                         WordIDPageID <PageId <WordID tf>>
 *                         WordtoWordID <Keyword WordID>
 *                         PageIDWordIDPos <PageID <WordID pos>>
 */



public class Score {

   static private HashMap<String, Integer> pageIdTable;
   static private HashMap<Integer,HashMap<Integer,ArrayList<Integer>>> titlePos;
   static private HashMap<Integer,HashMap<Integer,ArrayList<Integer>>> contentPos;
   static private HashMap<Integer, HashMap<Integer,Integer>> inverted_table_title;
   static private HashMap<Integer, HashMap<Integer,Integer>> inverted_table_content;
   static private HashMap<Integer,Integer> maxtfContent;
   //static private HashMap<Integer,Integer> maxtfTitle;
   static private HashMap<Integer, ArrayList<Double>> sizemaxtfContent;
   static private HashMap<Integer, ArrayList<Double>> sizemaxtfTitle;
   static private double N;

   static {
       try{
           ForwardFileforBody forwardFileforBody = new ForwardFileforBody("db/db_ForwardFileforBody");
           contentPos = forwardFileforBody.getHashMapTable();

           ForwardFileforTitle forwardFileforTitle = new ForwardFileforTitle("db/db_ForwardFileforTitle");
           titlePos = forwardFileforTitle.getHashMapTable();

           InvertFileforBody invertFileforBody = new InvertFileforBody("db/db_InvertFileforBody");
           inverted_table_content = invertFileforBody.getHashMapTable();

           InvertFileforTitle invertFileforTitle = new InvertFileforTitle("db/db_InvertFileforTitle");
           inverted_table_title = invertFileforTitle.getHashMapTable();

           PageIDtoBodyInfo pageIDtoBodyInfo = new PageIDtoBodyInfo("db/db_PageIDtoBodyInfo");
           sizemaxtfContent = pageIDtoBodyInfo.getHashMapTable();

           PageIDtoTitleInfo pageIDtoTitleInfo = new PageIDtoTitleInfo("db/db_PageIDtoTitleInfo");
           sizemaxtfTitle = pageIDtoTitleInfo.getHashMapTable();

           PageUrltoPageID pageUrlToPageID = new PageUrltoPageID("db/db_PageUrlToPageID");
           pageIdTable = pageUrlToPageID.getHashMapTable();

           N = pageIdTable.size();

       } catch (RocksDBException e) {
           e.printStackTrace();
       }
   }
    /**
     * Constructor
     */
    /**
     * Need to modified after having database
     */
    /*public Score() throws RocksDBException {
        //** Comment this part if have databse
        pageIdTable = new HashMap<>();
        pageIdTable.put("page A",1);
        pageIdTable.put("page B",2);
        pageIdTable.put("page C",3);
        pageIdTable.put("page D",4);
        int a = pageIdTable.size();
        N = (double) a;
        contentPos = new HashMap<>();
        /*HashMap<Integer,ArrayList<Integer>> keyPos = new HashMap<>();
        ArrayList<Integer> pos_1 = new ArrayList<>();
        pos_1.add(1);
        ArrayList<Integer> pos_2 = new ArrayList<>();
        pos_2.add(4); pos_2.add(5);
        keyPos.put(1,pos_1); keyPos.put(2,pos_2);*/
        /*HashMap<Integer,ArrayList<Integer>> keyPos2 = new HashMap<>();
        ArrayList<Integer> pos_3 = new ArrayList<>();
        pos_3.add(1);
        pos_3.add(6);
        keyPos2.put(2,pos_3);
        ArrayList<Integer> pos_4 = new ArrayList<>();
        pos_4.add(8);
        keyPos2.put(3,pos_4);
        HashMap<Integer,ArrayList<Integer>> keyPos3 = new HashMap<>();
        ArrayList<Integer> pos_5 = new ArrayList<>();
        pos_5.add(2);
        pos_5.add(5);
        pos_5.add(10);
        keyPos3.put(2,pos_5);

        ArrayList<Integer> pos_6 = new ArrayList<>();
        pos_6.add(2);
        pos_6.add(7);
        keyPos3.put(3,pos_6);

        contentPos.put(1,keyPos2);
        contentPos.put(2,keyPos3);


        /*contentPos.put(1,keyPos);
        contentPos.put(2,keyPos);
        contentPos.put(3,keyPos);*/
        /*inverted_table_content = new HashMap<>();
        HashMap<Integer,Integer> word1 = new HashMap<>();
        HashMap<Integer,Integer> word2 = new HashMap<>();
        HashMap<Integer,Integer> word3 = new HashMap<>();
        word3.put(1,1);
        word3.put(2,100);
        word2.put(3,1);
        word2.put(1,10);
        word2.put(2,100);
        word1.put(1,3);
        word1.put(2,3);
        word1.put(3,3);
        //inverted_table_content.put(1,word1);
        inverted_table_content.put(2,word3);
        inverted_table_content.put(3,word3);
        inverted_table_content.put(4,word1);
        //System.out.println(inverted_table_content.toString());
        ////////////////////////////////////////////////////////

    }*/
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

   public HashMap<Integer,Double> calculateScoreContent (String query) throws RocksDBException{

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

               //int m = maxtfContent.get(doc.getKey());
               //double maxtf = (double)m;
               double maxtf = sizemaxtfContent.get(doc.getKey()).get(1);
               double dsize = sizemaxtfContent.get(doc.getKey()).get(0);
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
       return sortByValue(result);

   }

    public HashMap<Integer,Double> calculateScoreCTitle (String query) throws RocksDBException{

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

                //int m = maxtfTitle.get(doc.getKey());
                //double maxtf = (double)m;
                double maxtf = sizemaxtfTitle.get(doc.getKey()).get(1);
                double dsize = sizemaxtfTitle.get(doc.getKey()).get(0);
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
        return sortByValue(result);

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
       System.out.println("Max: " + maxtfContent);

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
     * <PageID TF>
     */
    public Integer[] findPossiblePageID (String query) throws RocksDBException{

        if (query.length() == 0) return new Integer[0];

        Query q = new Query();
        ArrayList<Integer> paragh = q.convertToWordIDPhrase(query);
        ArrayList<Integer> distinct = q.getDistinctSetOfKeyword(paragh);
        ArrayList<Integer[]> docs = new ArrayList<>();
        for (int i = 0  ; i < distinct.size() ; i++)
            if(!inverted_table_content.containsKey(distinct.get(i))) return new Integer[0];

        for (int i = 0  ; i < distinct.size() ; i++) {
            HashMap<Integer,Integer> doc = inverted_table_content.get(distinct.get(i));
            Integer []  a = doc.keySet().toArray(new Integer[doc.size()]);
            docs.add(a);
        }

        int num = docs.size();
        Integer[] result = {};
        if (num > 2){
            Integer[] first = docs.get(0);
            Integer[] second = docs.get(1);
            Set<Integer> s1 = new HashSet<Integer>(Arrays.asList(first));
            Set<Integer> s2 = new HashSet<Integer>(Arrays.asList(second));
            s1.retainAll(s2);
            result = s1.toArray(new Integer[s1.size()]);
            int counter = 2;
            num = num-2;
            while (num != 0 ){
                Integer [] i = docs.get(counter);
                //System.out.println("Sep");
                //for ( Integer k : i) System.out.println(k);
                Set<Integer> s3 = new HashSet<Integer>(Arrays.asList(i));
                Set<Integer> s4 = new HashSet<Integer>(Arrays.asList(result));
                s3.retainAll(s4);
                result = s3.toArray(new Integer[s3.size()]);
                num = num-1;
                counter++;
            }
        }
        else if (num == 2){
            Integer[] first = docs.get(0);
            Integer[] second = docs.get(1);
            HashSet<Integer> set = new HashSet<>();
            set.addAll(Arrays.asList(first));
            set.retainAll(Arrays.asList(second));
            result = set.toArray(result);
        }
        else {
            if (num == 1 ) return docs.get(0);
            else return new Integer[0];
        }
        return result;

    }

    public Integer[] findPossiblePageIDTitle (String query) throws RocksDBException{

        if (query.length() == 0) return new Integer[0];

        Query q = new Query();
        ArrayList<Integer> paragh = q.convertToWordIDPhrase(query);
        ArrayList<Integer> distinct = q.getDistinctSetOfKeyword(paragh);
        ArrayList<Integer[]> docs = new ArrayList<>();
        for (int i = 0  ; i < distinct.size() ; i++)
            if(!inverted_table_title.containsKey(distinct.get(i))) return new Integer[0];

        for (int i = 0  ; i < distinct.size() ; i++) {
            HashMap<Integer,Integer> doc = inverted_table_title.get(distinct.get(i));
            Integer []  a = doc.keySet().toArray(new Integer[doc.size()]);
            docs.add(a);
        }

        int num = docs.size();
        Integer[] result = {};
        if (num > 2){
            Integer[] first = docs.get(0);
            Integer[] second = docs.get(1);
            Set<Integer> s1 = new HashSet<Integer>(Arrays.asList(first));
            Set<Integer> s2 = new HashSet<Integer>(Arrays.asList(second));
            s1.retainAll(s2);
            result = s1.toArray(new Integer[s1.size()]);
            int counter = 2;
            num = num-2;
            while (num != 0 ){
                Integer [] i = docs.get(counter);
                //System.out.println("Sep");
                //for ( Integer k : i) System.out.println(k);
                Set<Integer> s3 = new HashSet<Integer>(Arrays.asList(i));
                Set<Integer> s4 = new HashSet<Integer>(Arrays.asList(result));
                s3.retainAll(s4);
                result = s3.toArray(new Integer[s3.size()]);
                num = num-1;
                counter++;
            }
        }
        else if (num == 2){
            Integer[] first = docs.get(0);
            Integer[] second = docs.get(1);
            HashSet<Integer> set = new HashSet<>();
            set.addAll(Arrays.asList(first));
            set.retainAll(Arrays.asList(second));
            result = set.toArray(result);
        }
        else {
            return docs.get(0);
        }
        return result;

    }
    /**
     * finally add all the pos list together
     * check for start and end
     * @param query
     * @return
     */
    public ArrayList<Integer> pageHavePhraseContent (String query) throws RocksDBException{

        Query q = new Query();
        ArrayList<Integer> matchPage = new ArrayList<>();
        ArrayList<Integer> term = q.convertToWordIDPhrase(query);
        ArrayList<Integer> distinctSetOfKeyword = q.getDistinctSetOfKeyword(term);
        if (distinctSetOfKeyword.size() == 0 ) return matchPage;
        Integer stopNumStart = stopNumStart(term);
        Integer stopEndStart = stopNumEnd(term);
        ArrayList<Integer> trimStopStartEnd = trimStopStartEnd(term);
        System.out.println(trimStopStartEnd);
        Integer[] possiblePage = findPossiblePageID(query);
        for (Integer a : possiblePage) System.out.println("Possible PageObject: " + a);
        for (Integer page : possiblePage){
            System.out.println("PageObject: " + page);
            HashMap<Integer,ArrayList<Integer>> pagePos = contentPos.get(page);
            ArrayList<Integer> allPos = allThePos(pagePos);
            ArrayList<Integer> first_Keyword = pagePos.get(trimStopStartEnd.get(0));
            Integer suitable_s = -1;
            Integer suitable_e = -1;
            boolean containAll = false;
            for (int i = 0 ; i < first_Keyword.size() ; i++){
                Integer posNum = first_Keyword.get(i);
                System.out.println("Start:" + posNum);
                for (int j = 1 ; j < trimStopStartEnd.size() ; j++){
                    if (containAll) break;
                    Integer queryNextTerm = trimStopStartEnd.get(j);
                    System.out.println("QueryNextTerm: " + queryNextTerm);
                    posNum = posNum+1;
                    System.out.println("Pos: " + posNum);

                    if (queryNextTerm != -1){
                        System.out.println("queryNextTerm: " + queryNextTerm);
                        if (!pagePos.get(queryNextTerm).contains(posNum)) break;

                    }
                    if (queryNextTerm == -1) {
                        if (allPos.contains(posNum)) {
                            break;
                        }
                    }

                    if (j == trimStopStartEnd.size()-1){
                        System.out.println("Successful");
                        suitable_s = first_Keyword.get(i);
                        suitable_e = posNum;
                        containAll = true;
                    }

                }
            }
            //*where is the start*//
            if (containAll){
                for (int i = 1 ; i <= stopNumStart; i++){
                    System.out.println("first");
                    if (allPos.contains(suitable_s-i) || (suitable_s-i <= 0)) containAll=false;
                }
            }
            if (containAll){
                for (int i = 1 ; i <= stopEndStart; i++){
                    System.out.println("Second");
                    //if (allPos.contains(suitable_e+i) || (suitable_e+i > getMaxPos(pagePos))) containAll=false;
                    if (allPos.contains(suitable_e+i) ) containAll=false;

                }
            }

            if (containAll) matchPage.add(page);

        }
        return matchPage;
    }

    public ArrayList<Integer> pageHavePhraseTitle (String query) throws RocksDBException{

        Query q = new Query();
        ArrayList<Integer> matchPage = new ArrayList<>();
        ArrayList<Integer> term = q.convertToWordIDPhrase(query);
        ArrayList<Integer> distinctSetOfKeyword = q.getDistinctSetOfKeyword(term);
        if (distinctSetOfKeyword.size() == 0 ) return matchPage;
        Integer stopNumStart = stopNumStart(term);
        Integer stopEndStart = stopNumEnd(term);
        ArrayList<Integer> trimStopStartEnd = trimStopStartEnd(term);
        System.out.println(trimStopStartEnd);
        Integer[] possiblePage = findPossiblePageIDTitle(query);
        for (Integer a : possiblePage) System.out.println("Possible PageObject: " + a);
        for (Integer page : possiblePage){
            System.out.println("PageObject: " + page);
            HashMap<Integer,ArrayList<Integer>> pagePos = titlePos.get(page);
            ArrayList<Integer> allPos = allThePos(pagePos);
            ArrayList<Integer> first_Keyword = pagePos.get(trimStopStartEnd.get(0));
            Integer suitable_s = -1;
            Integer suitable_e = -1;
            boolean containAll = false;
            for (int i = 0 ; i < first_Keyword.size() ; i++){
                Integer posNum = first_Keyword.get(i);
                System.out.println("Start:" + posNum);
                for (int j = 1 ; j < trimStopStartEnd.size() ; j++){
                    if (containAll) break;
                    Integer queryNextTerm = trimStopStartEnd.get(j);
                    System.out.println("QueryNextTerm: " + queryNextTerm);
                    posNum = posNum+1;
                    System.out.println("Pos: " + posNum);

                    if (queryNextTerm != -1){
                        System.out.println("queryNextTerm: " + queryNextTerm);
                        if (!pagePos.get(queryNextTerm).contains(posNum)) break;

                    }
                    if (queryNextTerm == -1) {
                        if (allPos.contains(posNum)) {
                            break;
                        }
                    }

                    if (j == trimStopStartEnd.size()-1){
                        System.out.println("Successful");
                        suitable_s = first_Keyword.get(i);
                        suitable_e = posNum;
                        containAll = true;
                    }

                }
            }
            //*where is the start*//
            if (containAll){
                for (int i = 1 ; i <= stopNumStart; i++){
                    System.out.println("first");
                    if (allPos.contains(suitable_s-i) || (suitable_s-i <= 0)) containAll=false;
                }
            }
            if (containAll){
                for (int i = 1 ; i <= stopEndStart; i++){
                    System.out.println("Second");
                    //if (allPos.contains(suitable_e+i) || (suitable_e+i > getMaxPos(pagePos))) containAll=false;
                    if (allPos.contains(suitable_e+i) ) containAll=false;

                }
            }

            if (containAll) matchPage.add(page);

        }
        return matchPage;
    }

    public HashMap<Integer,Double> computeScorePhraseContent (ArrayList<Integer> matchPage , String query) throws RocksDBException{
        HashMap<Integer,Double> result = new HashMap<>();
        Query query1 = new Query();
        HashMap<Integer,Integer> queryterm = query1.convertToWordID(query);
        double qsize = query1.qLength(queryterm);

        for (Integer page : matchPage){
            //Double dsize = 10.0;
            for (Map.Entry<Integer,Integer> term : queryterm.entrySet()){
                HashMap<Integer,Integer> dochave = inverted_table_content.get(term.getKey());
                int f = dochave.size();
                double df = (double)f;
                int t = dochave.get(page);
                double tf = (double)t;

                //int m = maxtfContent.get(page);
                //double maxtf = (double)m;
                double maxtf = sizemaxtfContent.get(page).get(1);
                double dsize = sizemaxtfContent.get(page).get(0);
                double idf = Math.log(N/df)/Math.log(2);
                double dweight = (tf/maxtf) * idf;
                int qt = term.getValue();
                double qweight = (double)qt;
                if (!result.containsKey(page))
                    result.put(page,cos_sim(dweight,dsize,qweight,qsize));
                else
                    result.put(page,result.get(page)+cos_sim(dweight,dsize,qweight,qsize));


            }
        }
        return result;
    }

    public HashMap<Integer,Double> computeScorePhraseTitle (ArrayList<Integer> matchPage , String query) throws RocksDBException{
        HashMap<Integer,Double> result = new HashMap<>();
        Query query1 = new Query();
        HashMap<Integer,Integer> queryterm = query1.convertToWordID(query);
        double qsize = query1.qLength(queryterm);

        for (Integer page : matchPage){
            //Double dsize = 10.0;
            for (Map.Entry<Integer,Integer> term : queryterm.entrySet()){
                HashMap<Integer,Integer> dochave = inverted_table_title.get(term.getKey());
                int f = dochave.size();
                double df = (double)f;
                int t = dochave.get(page);
                double tf = (double)t;

                //int m = maxtfTitle.get(page);
                //double maxtf = (double)m;
                double maxtf = sizemaxtfTitle.get(page).get(1);
                double dsize = sizemaxtfTitle.get(page).get(0);
                double idf = Math.log(N/df)/Math.log(2);
                double dweight = (tf/maxtf) * idf;
                int qt = term.getValue();
                double qweight = (double)qt;
                if (!result.containsKey(page))
                    result.put(page,cos_sim(dweight,dsize,qweight,qsize));
                else
                    result.put(page,result.get(page)+cos_sim(dweight,dsize,qweight,qsize));


            }
        }
        return result;
    }


    public Integer stopNumStart (ArrayList <Integer> term){
        Integer count = 0;
        for (Integer i : term) {
            if (i == -1) count++;
            else break;
        }
        return count;

    }

    public Integer stopNumEnd (ArrayList <Integer> term) {
        Integer count = 0 ;
        for (int i = term.size()-1 ; i >= 0 ; i --) {
            if (term.get(i) == -1) count++;
            else break;
        }
        return count;
    }

    public ArrayList<Integer> trimStopStartEnd (ArrayList<Integer> term) {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0 ; i < term.size() ; i++) {
            if (term.get(i) != -1) break;
            else {
                term.remove(i);
                i = i - 1;
            }
        }
        for (int i = term.size()-1 ; i >= 0 ; i--) {
            if (term.get(i) != -1) break;
            else {
                term.remove(i);
            }
        }
        return term;
    }

    public ArrayList<Integer> allThePos (HashMap<Integer,ArrayList<Integer>> posList){
        ArrayList<Integer> result = new ArrayList<>();
        for (Map.Entry<Integer,ArrayList<Integer>> in : posList.entrySet()){
            result.addAll(in.getValue());
        }
        return result;
    }

    public HashMap<Integer,Double> allInOneComputePhraseScoreContent (String query) throws RocksDBException{
        ArrayList<Integer> pagematch = pageHavePhraseContent(query);
        HashMap<Integer,Double> result = computeScorePhraseContent(pagematch,query);
        return sortByValue(result);
    }

    public HashMap<Integer,Double> allInOneComputePhraseScoreTitle (String query) throws RocksDBException{
        ArrayList<Integer> pagematch = pageHavePhraseTitle(query);
        HashMap<Integer,Double> result = computeScorePhraseTitle(pagematch,query);
        return sortByValue(result);
    }

    public static HashMap<Integer,Double> sortByValue(HashMap<Integer,Double> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<Integer,Double> > list =
                new LinkedList<Map.Entry<Integer,Double> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<Integer,Double> >() {
            public int compare(Map.Entry<Integer,Double> o1,
                               Map.Entry<Integer,Double> o2)
            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<Integer,Double> temp = new LinkedHashMap<Integer,Double>();
        for (Map.Entry<Integer,Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }



    public HashMap<Integer,Double> ranking (String query) throws RocksDBException{
        HashMap<Integer,Double> title;
        HashMap<Integer,Double> content;
        if (Query.isPhraseSearch(query)) {
            query = query.substring(1, query.length() - 1);
            title = allInOneComputePhraseScoreTitle(query);
            content = allInOneComputePhraseScoreContent(query);
        }

        else {

            title = calculateScoreCTitle(query);
            content =calculateScoreContent(query);
        }

        for (Map.Entry<Integer,Double> in : title.entrySet())
            title.put(in.getKey(),in.getValue()*10);

        for (Map.Entry<Integer,Double> in : title.entrySet()) {
            if (content.containsKey(in.getKey()))
                content.put(in.getKey(), content.get(in.getKey()) + in.getValue());
            else
                content.put(in.getKey(),in.getValue());
        }
            return sortByValue(content);
    }

    public ArrayList<Integer> getTheKeyReverseOrder (HashMap<Integer,Double> content){
        ArrayList<Integer> reversekey = new ArrayList<>(content.keySet());
        Collections.reverse(reversekey);
        return reversekey;
    }

    public static void main (String [] a) throws RocksDBException{
       Score score = new Score();
       //System.out.println(score.findPossiblePageID("loving love love love love hong hong hong loves").toString());
       Integer[] p = score.findPossiblePageID("hong kong");
       for (Integer b : p) System.out.println(b);
       ArrayList<Integer> arr = new ArrayList<>();
       arr.add(-1);
       arr.add(-1);
       arr.add(-1);
       arr.add(1);
       arr.add(2);
       arr.add(3);
       arr.add(3);
       arr.add(-1);
       arr.add(3);
       arr.add(-1);
       arr.add(-1);
       arr.add(-1);
       arr.add(-1);

      //System.out.println(score.trimStopStartEnd(arr).toString());
      System.out.println(score.stopNumStart(arr).toString());
      System.out.println(score.stopNumEnd(arr).toString());
      System.out.println(score.pageHavePhraseContent("On hong in kong in"));
      HashMap<Integer,Double> k = score.allInOneComputePhraseScoreContent("On hong in kong in");
      System.out.println(score.computeScorePhraseContent(score.pageHavePhraseContent("On hong in kong in"),"On hong in kong in"));
      System.out.println(score.allInOneComputePhraseScoreContent("On hong in kong in love"));
      System.out.println("Hello " + k);
      Integer[] l = score.findPossiblePageID("hong kong cool");
      for (Integer b : l) System.out.println(b);
      System.out.println(l.length);

      //System.out.println("Hello " + score.allInOneComputePhraseScoreContent("On hong in kong in"));
      /*ArrayList<Integer> b = new ArrayList<>();
      b.add(-1);
      b.add(2);
      b.add(-1);
      b.add(3);
      b.add(-1);
      System.out.println(score.trimStopStartEnd(b));*/
      HashMap<Integer,Double> h = new HashMap<>();
      h.put(1,10.888);
      h.put(7,9.9);
      h.put(8,9.9);
      System.out.print(Score.sortByValue(h));




  }

}

