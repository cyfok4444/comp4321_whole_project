import java.util.ArrayList;
import java.util.HashMap;

public class ProcessString {

    public static HashMap<String,Integer> keyWord (ArrayList<String> input){
        HashMap<String,Integer> process = new HashMap<>();
        for (int i = 0 ; i < input.size() ; i ++){
            if (!process.containsKey(input.get(i))){
                process.put(input.get(i), 1);
            }

            else{
                process.put(input.get(i), process.get(input.get(i)) + 1);
            }
        }
        //System.out.println("Hello");
        return process;
    }

    public static ArrayList<String> removeRubbish(ArrayList<String> input){
        for (int i = 0 ; i < input.size() ; i ++) {
            //System.out.println("i: " + i + " Size:" + input.size());
            if (!(input.get(i).chars().allMatch(Character::isLetter))) {
                //System.out.println("Remove: " + input.get(i));
                input.remove(i);
                i = i - 1;
            }

        }
        for (int i = 0; i < input.size(); i++) {
            String k = input.get(i);
            for (int j = 0 ; j < k.length() ; j ++){
                char ch = k.charAt(j);
                if (Character.UnicodeScript.of(ch) == Character.UnicodeScript.HAN) {
                    input.remove(i);
                    i = i - 1;
                    break;
                }
            }
        }
        //else
        // System.out.println("Keep: " + input.get(i));



        return input;

    }

    public static ArrayList<String> stopWordRemove(ArrayList<String> input){
        StopStem stopStem = new StopStem("/Users/chunyinfok/Downloads/comp4321_project/src/stopword");
        for (int i = 0 ; i < input.size() ; i++){
            if (stopStem.isStopWord(input.get(i))){
                input.remove(i);
                i = i -1;
            }
        }
        return input;
    }


    public static void main (String [] args){
        ArrayList<String> k = new ArrayList<>();
        k.add("a");
        k.add("b");
        k.add("This");
        k.add(",,,");
        k.add("hiiiii");
        k.add(",,");
        k.add(",,");
        k.add(",,");
        k.add("神奇");
        k.add("jjjjjjjjjj");
        k.add("jjjjjjjjjj");
        k.add("lo");
        k.add("lo");
        k.add("你");


        k = removeRubbish(k);
        k = stopWordRemove(k);
        HashMap<String, Integer> a = keyWord(k);
        for (HashMap.Entry<String, Integer> entry : a.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            System.out.println("Key: " + key + " " + "value: " + value);

        }

    }

}
