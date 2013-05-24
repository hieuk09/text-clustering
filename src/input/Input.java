package input;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.*;

import data.Conversation;

public class Input {
	
	public static ArrayList<Object> readFromText(String fileName) {
		ArrayList<Object> aStrings = new ArrayList<Object>();
		try {
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			
			//Read File Line By Line
			while ((strLine = br.readLine()) != null)   {
				//Save content to an array
				aStrings.add(strLine);
			}
			
			//Close the input stream
			in.close();
		} catch (FileNotFoundException e) {
			System.err.println("File not found");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return aStrings;
	}
	
	public static ArrayList<Object> readFromConversation(String fileName) {
		ArrayList<Object> aConversations = new ArrayList<Object>();
		try {
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			//Read File Line By Line
			while ((br.readLine()) != null)   {
				//The first line is number
				
				Conversation con = new Conversation();
								
				con.setTopic(br.readLine()); 	//Second line is topic
				con.setQuestion(br.readLine());	//3rd line is question
				con.setAnswer(br.readLine());	//4th line is answer
				
				br.readLine();					//Last line is separator
				aConversations.add(con);
			}
			
			//Close the input stream
			in.close();
		} catch (FileNotFoundException e) {
			System.err.println("File not found");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return aConversations;
	}

}
