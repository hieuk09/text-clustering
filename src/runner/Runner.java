package runner;

import input.Input;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Vector;

import jvntagger.MaxentTagger;
import jvntagger.POSTagger;

import output.Output;
import output.XMLOutput;

import data.Conversation;

import processor.AimlConverter;
import processor.QuestionProcessor;
import processor.TextTokenizer;
import processor.Utility;
import svm.SvmProcessor;


public class Runner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//createRandomMatchedSentencen("data/pair.txt", "data/match-sentence.txt");
		
		//Convert conversation.txt to svm-feature.txt
		//convertCorpus2SVMFeature("data/input.txt", "data/SVM/svm-feature-match-class.txt");
		//convertCorpus2SVMFeature("data/match-sentence.txt", "data/SVM/svm-feature-match-sentence.txt");
		
		//Convert conversation.txt to conversation.aiml 
		//convertConversation2AIML("data/conversation.txt", "data/AIML/conversation.aiml");
		
		//Convert conversation.txt to bayes-feature.txt
		//convertCorpus2BayesFeature("data/conversation.txt", "data/Bayes/bayes-feature.txt");
		//convertCorpus2BayesFeature("data/match-sentence.txt", "data/Bayes/bayes-feature-match-sentence.txt");
		
		//Print command in AIML format 
		//printAIML();
		
		//createTrainAndTestFile("data/input.txt", "data/test.txt", "data/train.txt");
//		createSVMTrainingSet("data/Input", "data/SVM");
//		try {
//			SvmProcessor.svmTrain();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		String label = QuestionProcessor.getTrueQuestion("Ram của tôi bị hỏng rồi");
		System.out.println(label);
		
		//convertLabelToClass("data/predict-label.txt", "data/predict-class.txt");
		
//		AimlConverter.run();
//		AimlConverter.runForConversation();
		
		System.out.println("END");
	}
	
	//*****************************************************************
	//CREATE FEATURE FUNCTION
	
	public static void convertCorpus2SVMFeature(String fileIn, String fileOut) {
		preprocess(fileIn, "data/conversation-tag.txt");
		createDictionary("data/conversation-tag.txt");
		convertText2Feature("data/conversation-tag.txt", fileOut);
	}
	
	public static void convertCorpus2BayesFeature(String fileIn, String fileOut) {
		preprocess(fileIn, "data/conversation-tag.txt");
		createDictionary("data/conversation-tag.txt");
		convertText2BayesFeature("data/conversation-tag.txt", fileOut);
	}
	
	public static void convertCorpus2SVMFeatureNoDict(String fileIn, String fileOut) {
		preprocess(fileIn, "data/conversation-tag.txt");
		convertText2Feature("data/conversation-tag.txt", fileOut);
	}
	
	public static void convertCorpus2BayesFeatureNoDict(String fileIn, String fileOut) {
		preprocess(fileIn, "data/conversation-tag.txt");
		convertText2BayesFeature("data/conversation-tag.txt", fileOut);
	}
	
	public static void finalizeBayesFeature() {
		convertFeatureFromPCA("data/Bayes/bayes-feature.txt", "data/Bayes/feature-bayes-pca.txt");
		printLabel("data/conversation-tag.txt", "data/bayes-label.txt", "data/Bayes/feature-bayes-pca.txt");
	}
	
	//*****************************************************************
	//PREPROCESSING FUNCTION
	
	public static void convertLabelToClass(String fileIn, String fileOut) {
		ArrayList<Object> topics = Input.readFromText("data/topic.txt");

		ArrayList<Object> list = Input.readFromText(fileIn);
		Object[] objectArray = list.toArray();
		String[] labels = Arrays.copyOf(objectArray, objectArray.length, String[].class);

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileOut));

			for (int i = 0; i < labels.length; i++) {				
				Double number = Double.parseDouble(labels[i]);
				int index = number.intValue();
				String label = (String)topics.get(index);
				bw.write(label + "\n");
			}

			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void createSVMTrainingSet(String folderIn, String folderOut) {
		combineFile2Input(folderIn);	// "data/Input"
		String testFile = folderOut + "/test.txt";
		String trainFile = folderOut + "/train.txt";
		String inputFile = "data/input.txt";
		String tagFile = folderOut + "/tag.txt";
		String testFeature = folderOut + "/test-feature.txt";
		String trainFeature = folderOut + "/train-feature.txt";
		createTrainAndTestFile(inputFile, testFile, trainFile);
		preprocess(inputFile, tagFile);
		createDictionary(tagFile);
		convertCorpus2SVMFeatureNoDict(testFile, testFeature);
		convertCorpus2SVMFeatureNoDict(trainFile, trainFeature);
	}
	
	public static void combineFile(String fileIn1, String fileIn2, String fileOut) {
		//Read input
		ArrayList<Object> list1 = Input.readFromConversation(fileIn1);
		Object[] objectArray1 = list1.toArray();
		Conversation[] conversations1 = Arrays.copyOf(objectArray1, objectArray1.length, Conversation[].class);
		
		ArrayList<Object> list2 = Input.readFromConversation(fileIn2);
		Object[] objectArray2 = list2.toArray();
		Conversation[] conversations2 = Arrays.copyOf(objectArray2, objectArray2.length, Conversation[].class);
		
		Conversation[] conversations = concat(conversations1, conversations2);
		
		Output.writeConversation(conversations, fileOut);
	} 
	
	public static void createTrainAndTestFile(String fileIn, String fileOut1, String fileOut2) {
		//Read input
		ArrayList<Object> list = Input.readFromConversation(fileIn);
		Object[] objectArray = list.toArray();
		Conversation[] conversations = Arrays.copyOf(objectArray, objectArray.length, Conversation[].class);

		int length = conversations.length;
		int lengthTest = length / 4;
		int lengthTrain = length - lengthTest;		
		
		Conversation[] test = new Conversation[lengthTest];
		Conversation[] train = new Conversation[lengthTrain];
		boolean[] check = new boolean[length];
		Random randomGenerator = new Random();
		
		for (int i = 0; i < lengthTest; i++) {
			int randomInt = 0;
			do {
				randomInt = randomGenerator.nextInt(length);
			} while(check[randomInt]);
			test[i] = conversations[randomInt];
			check[randomInt] = true;
		}
		
		int count = 0;
		for (int i = 0; i < length; i++) {
			if (!check[i]) {
				train[count] = conversations[i];
				count++;
			}
		}
		
		
		Output.writeConversation(test, fileOut1);
		Output.writeConversation(conversations, fileOut2);
	}
	
	public static void preprocess(String fileIn, String fileOut) {
		tokenizeText(fileIn, "data/temp/raw.txt");
		removeUnwantedWords("data/temp/raw.txt", fileOut);		
	}
	
	public static void tokenizeText(String fileIn, String fileOut) {
		//Read input
		ArrayList<Object> stringList = Input.readFromText(fileIn);
		Object[] objectArray = stringList.toArray();
		String[] strings = Arrays.copyOf(objectArray, objectArray.length, String[].class);

		//Print output
		for (int i = 0; i < strings.length; i++)
		{
			int index = i % 5;
			if (index == 0 || index == 4)
				continue;
			String[] tokens = TextTokenizer.tokenize(strings[i]);

			strings[i] = joinString(tokens, ' ');
			System.out.println(strings[i]);
		}

		Output.writeText(strings, fileOut);
	}
	
	public static void removeStopWord(String fileIn, String fileOut) {
		//Read input
		ArrayList<Object> stringList = Input.readFromText(fileIn);
		Object[] objectArray = stringList.toArray();
		String[] strings = Arrays.copyOf(objectArray, objectArray.length, String[].class);

		ArrayList<Object> blacklist = Input.readFromText("data/blacklist.txt");

		//Print output
		for (int i = 0; i < strings.length; i++)
		{
			String[] words = strings[i].split(" ");
			for (int j = 0; j < words.length; j++) {
				words[j] = words[j].toLowerCase();
				if (blacklist.contains(words[j])) {
					words[j] = "";
				}
			}
			strings[i] = joinString(words, ' ');

			System.out.println(strings[i]);
		}

		Output.writeText(strings, fileOut);
	}
	
	public static void createDictionary(String fileIn) {
		HashSet<String> dictionary = new HashSet<String>();
		HashSet<String> topics = new HashSet<String>();
		//Read input
		ArrayList<Object> list = Input.readFromConversation(fileIn);
		Object[] objectArray = list.toArray();
		Conversation[] conversations = Arrays.copyOf(objectArray, objectArray.length, Conversation[].class);

		//Print output
		for (int i = 0; i < conversations.length; i++)
		{
			topics.add(conversations[i].getTopic().toLowerCase());
			addString2Dict(conversations[i].getAnswer(), dictionary);
			addString2Dict(conversations[i].getQuestion(), dictionary);
		}

		Output.writeList(dictionary, "data/dictionary.txt");
		Output.writeList(topics, "data/topic.txt");
	}
	
	public static void convertText2Feature(String fileIn, String fileOut) {
		ArrayList<Object> dictionary = Input.readFromText("data/dictionary.txt");
		ArrayList<Object> topics = Input.readFromText("data/topic.txt");

		ArrayList<Object> list = Input.readFromConversation(fileIn);
		Object[] objectArray = list.toArray();
		Conversation[] conversations = Arrays.copyOf(objectArray, objectArray.length, Conversation[].class);

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileOut));
			HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();

			for (int i = 0; i < conversations.length; i++) {				
				map.clear();				
				
				//String[] answer = conversations[i].getAnswer().split(" ");
				String[] question = conversations[i].getQuestion().split(" ");

				//Utility.putIndex2Map(answer, map, dictionary);
				Utility.putIndex2Map(question, map, dictionary);

				Integer topic_index = topics.indexOf(conversations[i].getTopic());

				bw.write(topic_index.toString());

				Collection<Integer> unsorted = map.keySet();
				List<Integer> sorted = asSortedList(unsorted);
				Iterator<Integer> iter = sorted.iterator();
				while (true) {
					try {
						Integer index = (Integer)iter.next();
						Double value = (double) (map.get(index)*1.0 / (question.length));
						bw.write(" " + index.toString() + ":" + value.toString());

					} catch (NoSuchElementException ex) {
						break;
					}
				}
				bw.write("\n");

			}

			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public static void removeUnwantedWords(String fileIn, String fileOut) {
		//Read input
		ArrayList<Object> stringList = Input.readFromText(fileIn);
		Object[] objectArray = stringList.toArray();
		String[] strings = Arrays.copyOf(objectArray, objectArray.length, String[].class);

		String modelDir = "model/maxent";
		POSTagger tagger = new MaxentTagger(modelDir);;

		//Print output
		for (int i = 0; i < strings.length; i++)
		{
			int index = i % 5;
			if (index == 0 || index == 4 || index == 1)
			{
				strings[i] = strings[i].toLowerCase();
				continue;
			}
			String tokens = tagger.tagging(strings[i]);

			strings[i] = removeWord(tokens);
			System.out.println(strings[i]);
		}

		Output.writeText(strings, fileOut);
	}
	
	public static void convertText2BayesFeature(String fileIn, String fileOut) {
		ArrayList<Object> dictionary = Input.readFromText("data/dictionary.txt");

		ArrayList<Object> list = Input.readFromConversation(fileIn);
		Object[] objectArray = list.toArray();
		Conversation[] conversations = Arrays.copyOf(objectArray, objectArray.length, Conversation[].class);
		double min = 0.000001;

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileOut));
			HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();

			for (int i = 0; i < conversations.length; i++) {				
				map.clear();				

				//String[] answer = conversations[i].getAnswer().split(" ");
				String[] question = conversations[i].getQuestion().split(" ");

				//Utility.putIndex2Map(answer, map, dictionary);
				Utility.putIndex2Map(question, map, dictionary);

				//bw.write(topic_index.toString());

				Iterator<Object> iter = dictionary.iterator();
				while (true) {
					try {
						String word = iter.next().toString();
						Integer index = dictionary.indexOf(word);
						double value = min;
						Integer word_value = map.get(index);
						if (word_value != null)
							value = (double)word_value;
						bw.write(" " + Double.toString(value));

					} catch (NoSuchElementException ex) {
						break;
					}
				}
				bw.write("\n");

			}

			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public static void convertFeatureFromPCA(String fileIn, String fileOut) {
		//Read input
		ArrayList<Object> stringList = Input.readFromText(fileIn);
		Object[] objectArray = stringList.toArray();
		String[] strings = Arrays.copyOf(objectArray, objectArray.length, String[].class);
		
		ArrayList<Object> pcaList = Input.readFromText("data/feature-pca.txt");
		Object[] pcaArray = pcaList.toArray();
		String[] pca = Arrays.copyOf(pcaArray, pcaArray.length, String[].class);
		
		//Load data from pca to array
		Double[][] pcaParams = new Double[pca.length][];		
		for (int i = 0; i < pca.length; i++) {
			
			String[] yPCA = pca[i].split(" ");
			pcaParams[i] = new Double[yPCA.length];
			
			for (int j = 0; j < yPCA.length; j++) {
				pcaParams[i][j] = Double.parseDouble(yPCA[j]);
			}
			
		}

		//Convert input from string => array then print
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileOut));
			
			for (int i = 0; i < strings.length; i++)
			{
				strings[i] = strings[i].trim();
				String[] xArray = strings[i].split(" ");
				Double[] features = new Double[xArray.length];

				for (int j = 0; j < xArray.length; j++) {
					features[j] = Double.parseDouble(xArray[j]);
				}

				for (int j = 0; j < pca.length; j++) {
					Double number = 0.0;
					for (int k = 0; k < xArray.length; k++) {
						number += pcaParams[j][k] * features[k];
					}
					bw.write(number.toString() + " ");
				}
				bw.write('\n');
			}
			
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public static void printLabel(String fileIn, String fileOut, String featureFileOut) {
		ArrayList<Object> topics = Input.readFromText("data/topic.txt");

		ArrayList<Object> list = Input.readFromConversation(fileIn);
		Object[] objectArray = list.toArray();
		Conversation[] conversations = Arrays.copyOf(objectArray, objectArray.length, Conversation[].class);
		Integer[] topic_nums = new Integer[100000];

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileOut));
			BufferedWriter featureWriter = new BufferedWriter(new FileWriter(featureFileOut, true));

			for (int i = 0; i < conversations.length; i++) {				
				Integer topic_index = topics.indexOf(conversations[i].getTopic());
				if (topic_nums[topic_index] == null)
					topic_nums[topic_index] = 1;
				else
					topic_nums[topic_index]++;
					
				bw.write(topic_index.toString());
				bw.write("\n");
			}
			
			String defaultString = "";
			for (int i = 0; i < 159; i++) {
				defaultString += "1 ";
			}
			
			int count = 0;
			for (int i = 0; i < 100000; i++)
				if (topic_nums[i] != null && topic_nums[i] == 1)
				{					
					count++;
					bw.write(Integer.toString(i) + "\n");					
					featureWriter.write(defaultString + "\n");
				}
			
			System.out.println(count);

			bw.flush();
			bw.close();
			featureWriter.flush();
			featureWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public static void createRandomMatchedSentencen(String fileIn, String fileOut) {
		//Read input
		ArrayList<Object> stringList = Input.readFromText(fileIn);
		Object[] objectArray = stringList.toArray();
		String[] strings = Arrays.copyOf(objectArray, objectArray.length, String[].class);


		//Print output
		int length = strings.length / 3 * 5;
		String[] outputArray = new String[length];
		for (int i = 0; i < strings.length; i+=3)
		{
			int index = i / 3 * 5;
			double rand = Math.random();			
			outputArray[index] = Integer.toString(index / 5 + 1);
			outputArray[index + 1] = rand > 0.8 ? "1" : "0";
			outputArray[index + 2] = strings[i];
			outputArray[index + 3] = strings[i + 1];						
			outputArray[index + 4] = strings[i + 2];
		}

		Output.writeText(outputArray, fileOut);
	}
	
	//*****************************************************************
	//UTILITY FUNCTION
	
	public static void combineFile2Input(String folderIn) {
		try {
			File file = new File("data/input.txt");
			
			if (file.exists())
				file.delete();
			
			file.createNewFile();
			combineFilesForFolder(new File(folderIn));
		} catch (IOException e) {
		}	
	}
	
	public static void combineFilesForFolder(final File folder) {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				//listFilesForFolder(fileEntry);
			} else {
				System.out.println(fileEntry.getName());
				combineFile("data/input.txt", fileEntry.getPath(), "data/input.txt");				
			}
		}
	}
	
	public static <T> T[] concat(T[] first, T[] second) {
		T[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

	public static String joinString(String[] strings, char character) {
		String result = "";
		for (int i = 0; i < strings.length; i++)
			if (strings[i] != "")
				result += strings[i] + character;
		return result.trim().replaceAll(" +", " ");
	}	

	public static void addString2Dict(String string, HashSet<String> dict) {
		String[] strings = string.split(" ");	
		for (int  i = 0; i < strings.length; i++)			
			dict.add(strings[i].toLowerCase());
	}

	public static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
		List<T> list = new ArrayList<T>(c);
		java.util.Collections.sort(list);
		return list;
	}
	
	public static String removeWord(String string) {
		String[] array = string.split(" ");
		String result = "";
		for (int i = 0; i < array.length; i++) {
			if ((array[i].contains("/R") || 
					array[i].contains("/E") || array[i].contains("/P") ||
					array[i].contains("/C") || array[i].contains("/L") ||
					array[i].contains("/T") || array[i].contains("/I")) != true) {
				
				result += array[i].replaceAll("/([^/]*)$", "") + " ";
			}
				
		}
		return result.trim().toLowerCase();
	}	
	
	//*****************************************************************
	//AIML
	
	public static void printAIML() {
		//4 conditions => * X | X * | X | * X *
		String[] prefix = new String[]{"* ", "", "* "};
		String[] postfix = new String[]{"", "", " *"};
		//		String[] prefix = new String[]{"* ", ""};
		//		String[] postfix = new String[]{"", ""};
		String[] keyword_1 = new String[]{"BẬT", "MỞ"};
		String[] keyword_2 = new String[]{"BẢN ĐỒ", "MAP", "GOOGLE MAP"};
		String atom = "BẢN ĐỒ";
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("data/AIML/output.aiml"));
			for (int i = 0; i < prefix.length; i++) {

				for (int j = 0; j < keyword_1.length; j++) {
					for (int l = 0; l < keyword_2.length; l++) {
						String result = prefix[i] + keyword_1[j] + " " + keyword_2[l] + postfix[i];
						if (result.equalsIgnoreCase(atom)) {
							continue;
						}
						bw.write("\t<category>\n");
						bw.write("\t\t<pattern>" + result + "</pattern>\n");
						bw.write("\t\t<template>\n");
						bw.write("\t\t\t<srai>" + atom + "</srai>\n");
						bw.write("\t\t</template>\n");
						bw.write("\t</category>\n");
					}
				}				
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void convertConversation2AIML(String fileIn, String fileOut) {
		//Read input
		ArrayList<Object> list = Input.readFromConversation(fileIn);
		Object[] objectArray = list.toArray();
		Conversation[] conversations = Arrays.copyOf(objectArray, objectArray.length, Conversation[].class);
		HashMap<String, Vector<Conversation>> map = new HashMap<String, Vector<Conversation>>();
		//Print output

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileOut));
			map.clear();
			for (int i = 0; i < conversations.length; i++) {
				String topic = conversations[i].getTopic();
				if (map.containsKey(topic) == false) {
					Vector<Conversation> vector = new Vector<Conversation>();
					vector.add(conversations[i]);
					map.put(topic, vector);
				} else {
					Vector<Conversation> vector = map.get(topic);
					vector.add(conversations[i]);
					map.put(topic, vector);
				}
			}
			
			Collection<String> unsorted = map.keySet();
			List<String> sorted = asSortedList(unsorted);
			Iterator<String> iter = sorted.iterator();
			while (true) {
				try {
					String topic = (String)iter.next();
					
					Vector<Conversation> cons = map.get(topic);
					Iterator<Conversation> consIndex = cons.iterator();
					
				} catch (NoSuchElementException ex) {
					break;
				}
			}
			
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
