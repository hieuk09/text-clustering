package processor;

import input.Input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import jvntagger.MaxentTagger;
import jvntagger.POSTagger;

import output.XMLOutput;

import data.Aiml;
import data.Conversation;
import data.Category;

public class AimlConverter {
	
	public static Conversation[] readInput(String fileIn) {
		//Read input
		ArrayList<Object> stringList = Input.readFromConversation(fileIn);
		Object[] objectArray = stringList.toArray();
		Conversation[] conversations = Arrays.copyOf(objectArray, objectArray.length, Conversation[].class);

		String modelDir = "model/maxent";
		POSTagger tagger = new MaxentTagger(modelDir);;

		//Print output
		for (int i = 0; i < conversations.length; i++)
		{
			String topic = conversations[i].getTopic().toLowerCase();
			String question = conversations[i].getQuestion();
			String[] tokens = TextTokenizer.tokenize(question);
			question = Utility.joinString(tokens, ' ');
			question = question.replaceAll("[^\\w\\s\\u0081-\\u8888]", " ");
			question = tagger.tagging(question);
			question = Utility.replaceUnwantedWord(question, "*");
			question = question.replace("_", " ");

			conversations[i].setTopic(topic);
			conversations[i].setQuestion(question.toUpperCase());
		}
		return conversations;
	}
	
	public static ArrayList<Aiml> convert(Conversation conversations[]) {
		ArrayList<Aiml> categories = new ArrayList<Aiml>();
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		int count = 0;
		
		//Puts all dialogs with same topic to same category
		for (int i = 0; i < conversations.length; i++) {
			String topic = conversations[i].getTopic();
			
			if (map.containsKey(topic)) {				
				Integer index = map.get(topic);
				Aiml cat = categories.get(index);
				cat.addCategory(conversations[i]);
			} else {
				//if there isn't any category yet, 
				//create one, and add it to the hashmap
				Category cat = new Category(conversations[i]);
				Aiml aiml = new Aiml(cat, topic);
				categories.add(aiml);
				map.put(topic, count);
				count++;				
			}
		}
		return categories;
	}
	
	public static void writeOutput(ArrayList<Aiml> categories) {
		String folderPath = "data/XML";
		int count = 0;
		
		for (Aiml aiml : categories) {
			String filePath = folderPath + "/" + Integer.toString(count) + ".aiml";			
			XMLOutput.save(filePath, aiml);
			count++;
		}
		
//		AimlFile aimlFile = new AimlFile();
//
//		for (Aiml aiml : categories) {
//			aimlFile.addAiml(aiml);
//		}
//
//		XMLOutput.save("data/all.xml", aimlFile);
	}
	
	public static void run() {
		Conversation[] conversations = readInput("data/input.txt");
		ArrayList<Aiml> categories = convert(conversations);	
		writeOutput(categories);
	}
}
