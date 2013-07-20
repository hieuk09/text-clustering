package runner;

import input.Input;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import output.Output;

import processor.Utility;

public class Validation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		runTest("data/Test/result_alice_translate_voz.txt", "data/Test/percent_alice_translate_voz.txt");
		runTest("data/Test/result_alice_translate_wiki.txt", "data/Test/percent_alice_translate_wiki.txt");
		runTest("data/Test/result_alice_translate_yahoo.txt", "data/Test/percent_alice_translate_yahoo.txt");
	}
	
	public static void runTest(String fileIn, String fileOut) {
		ArrayList<Object> list = Input.readFromText(fileIn);
		ArrayList<String> questions = new ArrayList<String>();
		ArrayList<String> answers = new ArrayList<String>();
		int count = 0;
		for (Object s : list) {
			switch (count % 4) {
			case 1:
				questions.add((String)s);
				break;
			case 2:
				answers.add((String)s);
				break;
			}
			count++;
		}
		
		count = 0;
		String[] result = new String[questions.size()];
		
		for (String question : questions) {
			String answer = answers.get(count);
			int percentage = Utility.similarPercentage(answer, question);
			result[count] = Integer.toString(percentage);
			count++;
			if (count == questions.size())
				break;
		}		
		Output.writeText(result, fileOut);
	}

}
