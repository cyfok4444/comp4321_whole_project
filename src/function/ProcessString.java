package function;

import java.util.*;
import java.util.HashMap;
import java.util.LinkedHashMap;


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

    /*public static ArrayList<String> stopWordRemoveTf(ArrayList<String> input){
        StopStem stopStem = new StopStem("stopword.txt");
        for (int i = 0 ; i < input.size() ; i++){
            if (stopStem.isStopWord(input.get(i))){
                input.remove(i);
                i = i -1;
            }
        }
        return input;
    }*/

    public static HashMap<String,ArrayList<Integer>> stopWordRemovePos(HashMap<String,ArrayList<Integer>> input){
        StopStem stopStem = new StopStem("stopword.txt");
        ArrayList<String> delete = new ArrayList<>();
        for (Map.Entry<String,ArrayList<Integer>> entry : input.entrySet()){
            //System.out.println(entry.getKey());
            if (stopStem.isStopWord(entry.getKey())){
                //System.out.println(entry.getKey());
                delete.add(entry.getKey());
            }
        }
        for (String s : delete) input.remove(s);
        return input;
    }

    public static ArrayList<String> keyWordTf (ArrayList<String> input){
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0 ; i < input.size() ; i++){
            StopStem stopStem = new StopStem("stopword.txt");
            if (input.get(i)=="" || input.get(i).toLowerCase()==""){
                return result;
            }
            String word = stopStem.stem(input.get(i).toLowerCase());
            result.add(word);


        }
        return result;
    }
    public static HashMap<String,Integer> stopWordRemoveTf (ArrayList<String> input){
        HashMap<String,Integer> process = new HashMap<>();
        for (int i = 0 ; i < input.size() ; i++){
            StopStem stopStem = new StopStem("stopword.txt");
            if (input.get(i)=="" || input.get(i).toLowerCase()==""){
                return process;
            }
            if  (stopStem.isStopWord(input.get(i).toLowerCase())) continue;

            else {
                if (!process.containsKey(input.get(i).toLowerCase())) {
                    process.put(input.get(i).toLowerCase(), 1);
                }
                else {
                    process.put(input.get(i).toLowerCase(), process.get(input.get(i).toLowerCase()) + 1);
                }
            }

        }
        System.out.println(process);
        //System.out.println("good");
        return process;
    }
    /*
    public static HashMap<String,Integer> keyWordTf (ArrayList<String> input){
        HashMap<String,Integer> process = new HashMap<>();
        for (int i = 0 ; i < input.size() ; i++){
            StopStem stopStem = new StopStem("stopword.txt");
            if (input.get(i)=="" || input.get(i).toLowerCase()==""){
                return process;
            }
            String word = stopStem.stem(input.get(i).toLowerCase());
            if (!process.containsKey(word)){
                process.put(word, 1);
            }

            else{
                process.put(word, process.get(word) + 1);
            }

        }
        return process;
    }*/

    public static LinkedHashMap<String,ArrayList<Integer>> keyWordPos (ArrayList<String> input){
        //for (String in : input) System.out.println(in);
        LinkedHashMap<String,ArrayList<Integer>> process = new LinkedHashMap<>();
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

    public static ArrayList<String> doKeywordOnly (ArrayList<String> input){
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0 ; i < input.size() ; i ++){
            StopStem stopStem = new StopStem("stopword.txt");
            String word = stopStem.stem(input.get(i).toLowerCase());
            result.add(word);
        }

        return result;
    }
    public static void main (String [] args){
        ArrayList<String> k = new ArrayList<>();

        //k.add("love");
        //k.add("ABCD");
        //k.add("loves");
        k.add("School");
        k.add("ones");
        k.add("Engineering");

        ArrayList<String> a = keyWordTf(k);
        HashMap<String,Integer> b = stopWordRemoveTf(a);
        System.out.println(b);

        //k = removeRubbish(k);
        //HashMap<String,ArrayList<Integer>> y = keyWordPos(k);
        //HashMap<String,ArrayList<Integer>> i = stopWordRemovePos(y);
        //System.out.println(i);
        /*for (HashMap.Entry<String,ArrayList<Integer>> entry : y.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }*/

        //System.out.println("");

        /*HashMap<String,ArrayList<Integer>> l = stopWordRemovePos(y);
        for (HashMap.Entry<String,ArrayList<Integer>> entry : l.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }*/
        /*HashMap<String,Integer> h = ProcessString.keyWordTf(k);
        for (HashMap.Entry<String,Integer> entry : h.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }*/
        /*HashMap<String, ArrayList<Integer>> a = keyWordPos(k);
        for (HashMap.Entry<String, ArrayList<Integer>> entry : a.entrySet()) {
            String key = entry.getKey();
            ArrayList<Integer> value = entry.getValue();
            System.out.println("Key: " + key + " " + "value: " + value.toString());

        }*/

    }

}
