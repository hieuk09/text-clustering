package processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import jvntagger.MaxentTagger;
import jvntagger.POSTagger;

public class Utility {
	
	//Return whether two string is similar or not
	//2 string is similar if one of them contain another.
	//For example: 
	//	I am 10 years old
	//	I am 10 *
	//	* 10 *
	public static boolean isSimilar(String first, String second) {
		String[] firstArray = first.split(" ");
		String[] secondArray = second.split(" ");
		
		String[] longArr;
		String[] shortArr;
		
		//The short array is the base of the long array
		if (firstArray.length > secondArray.length) {
			longArr = firstArray;
			shortArr = secondArray;
		} else if (firstArray.length < secondArray.length) {
			shortArr = firstArray;
			longArr = secondArray;
		} else {
			if (first.contains("*")) {
				shortArr = firstArray;
				longArr = secondArray;
			} else {
				longArr = firstArray;
				shortArr = secondArray;
			}
		}
		
		int count = 0;
		boolean valid = false;
		
		for (int i = 0; i < shortArr.length; i++) {
			if (shortArr[i].equals("*")) {
				if (i == shortArr.length - 1) {					
					valid = true;
					break;
				} else {
					for (int j = count + 1; j < longArr.length; j++) {
						if (longArr[j].equals(shortArr[i + 1])) {
							count = j;
							break;
						}
					}									
					
					if (count == longArr.length) {						
						valid = false;
						break;
					}
				}
			} else if (shortArr[i].equals(longArr[count])) {
				count++;
				if (count == longArr.length) {
					valid = i == shortArr.length - 1;
					break;
				}
				
			} else {
				valid = false;
				break;
			}
			
		}
		
		return valid;
	}

	public static String replaceUnwantedWord(String sentence, String replacement) {
		String[] array = sentence.split(" ");
		String result = "";
		for (int i = 0; i < array.length; i++) {
			if ((array[i].contains("/R") || 
					array[i].contains("/E") || array[i].contains("/P") ||
					array[i].contains("/C") || array[i].contains("/L") ||
					array[i].contains("/T") || array[i].contains("/I")) != true) {
				
				result += array[i].replaceAll("/([^/]*)$", "") + " ";
			} else {
				result += replacement + " ";
			}
				
		}
		return result.trim().toLowerCase();
	}
	
	public static String joinString(String[] strings, char character) {
		String result = "";
		for (int i = 0; i < strings.length; i++)
			if (strings[i] != "")
				result += strings[i] + character;
		return result.trim().replaceAll(" +", " ");
	}
	
	public static void putIndex2Map(String[] string, HashMap<Integer, Integer> map, ArrayList<Object> dictionary) {
		if (string.length == 1 && string[0].length() == 0)
			return;
		for (int j = 0; j < string.length; j++) {
			int index = dictionary.indexOf(string[j]);

			if (map.containsKey(index)) {
				int value = map.get(index);
				map.put(index, value + 1);
			}
			else
				map.put(index, 1);
		}
	}
	
	public static int random(int length) {
		Random randomGenerator = new Random();
		return randomGenerator.nextInt(length);
	}
	
	public static int similarPercentage(String answer, String question) {		
		String[] tokens = TextTokenizer.tokenize(question);
		question = Utility.joinString(tokens, ' ');
		question = question.replaceAll("[^\\w\\s\\u0081-\\u8888]", " ");
		question = Tagger.tagging(question);
		question = Utility.replaceUnwantedWord(question, "").replaceAll("\\s+", " ");
		
		tokens = TextTokenizer.tokenize(answer);
		answer = Utility.joinString(tokens, ' ');
		answer = answer.replaceAll("[^\\w\\s\\u0081-\\u8888]", " ");
		answer = Tagger.tagging(answer);
		answer = Utility.replaceUnwantedWord(answer, "").replaceAll("\\s+", " ");
		
		List<String> questions = Arrays.asList(question.split(" "));
		List<String> answers = Arrays.asList(answer.split(" "));
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		int count = 0;
		for (String s : questions) {			
			if (answers.indexOf(s) >= 0 && map.containsKey(s) == false)
				count++;
			map.put(s, 1);
		}
		
		return count * 100 / answers.size();
	}
}
