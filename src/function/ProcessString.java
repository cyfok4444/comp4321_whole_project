package function;

import java.util.ArrayList;
import java.util.HashMap;

public class ProcessString {


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

        return input;

    }

    public static ArrayList<String> stopWordRemove(ArrayList<String> input){
        StopStem stopStem = new StopStem("stopword.txt");
        for (int i = 0 ; i < input.size() ; i++){
            if (stopStem.isStopWord(input.get(i))){
                input.remove(i);
                i = i -1;
            }
        }
        return input;
    }

    public static HashMap<String,Integer> keyWord (ArrayList<String> input){
        HashMap<String,Integer> process = new HashMap<>();
        for (int i = 0 ; i < input.size() ; i++){
            StopStem stopStem = new StopStem("stopword.txt");
            String word = stopStem.stem(input.get(i));
            if (!process.containsKey(word)){
                process.put(input.get(i), 1);
            }

            else{
                process.put(word, process.get(word) + 1);
            }

        }
        return process;
    }


    public static void main (String [] args){
        ArrayList<String> k = new ArrayList<>();

        k.add("love");
        k.add("loves");
        k.add("loving");




        k = removeRubbish(k);
        k = stopWordRemove(k);
        for (String s : k){
            System.out.println(s);
        }
        HashMap<String, Integer> a = keyWord(k);
        for (HashMap.Entry<String, Integer> entry : a.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            System.out.println("Key: " + key + " " + "value: " + value);

        }

    }

}
