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

    public static ArrayList<String> stopWordRemoveTf(ArrayList<String> input){
        StopStem stopStem = new StopStem("stopword.txt");
        for (int i = 0 ; i < input.size() ; i++){
            if (stopStem.isStopWord(input.get(i))){
                input.remove(i);
                i = i -1;
            }
        }
        return input;
    }

    public static HashMap<String,ArrayList<Integer>> stopWordRemovePos(HashMap<String,ArrayList<Integer>> input){
        StopStem stopStem = new StopStem("stopword.txt");
        for (HashMap.Entry<String,ArrayList<Integer>> entry: input.entrySet()){
            if (stopStem.isStopWord(entry.getKey())){
                input.remove(entry.getKey());
            }
        }
        return input;
    }

    public static HashMap<String,Integer> keyWordTf (ArrayList<String> input){
        HashMap<String,Integer> process = new HashMap<>();
        for (int i = 0 ; i < input.size() ; i++){
            StopStem stopStem = new StopStem("stopword.txt");
            String word = stopStem.stem(input.get(i).toLowerCase());
            if (!process.containsKey(word)){
                process.put(word, 1);
            }

            else{
                process.put(word, process.get(word) + 1);
            }

        }
        return process;
    }

    public static HashMap<String,ArrayList<Integer>> keyWordPos (ArrayList<String> input){
        for (String in : input) System.out.println(in);
        HashMap<String,ArrayList<Integer>> process = new HashMap<>();
        for (int i = 0 ; i < input.size() ; i++){
            StopStem stopStem = new StopStem("stopword.txt");
            String word = stopStem.stem(input.get(i).toLowerCase());
            if (!process.containsKey(word)){
                ArrayList<Integer> pos = new ArrayList<>();
                pos.add(i+1);
                process.put(word,pos);
            }

            else{
                ArrayList<Integer> pos = process.get(word);
                pos.add(i+1);
                process.put(word,pos);
            }

        }
        return process;
    }
    public static void main (String [] args){
        ArrayList<String> k = new ArrayList<>();

        k.add("love");
        k.add("ABCD");
        k.add("loves");
        k.add("loving");

        k = removeRubbish(k);
        k = stopWordRemoveTf(k);
        HashMap<String,Integer> h = ProcessString.keyWordTf(k);
        for (HashMap.Entry<String,Integer> entry : h.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        /*HashMap<String, ArrayList<Integer>> a = keyWordPos(k);
        for (HashMap.Entry<String, ArrayList<Integer>> entry : a.entrySet()) {
            String key = entry.getKey();
            ArrayList<Integer> value = entry.getValue();
            System.out.println("Key: " + key + " " + "value: " + value.toString());

        }*/

    }

}
