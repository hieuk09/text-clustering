package output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

import data.Conversation;

public class Output {
	
	public static void writeList(HashSet<String> data, String sFileName) {
		Iterator<String> iter = data.iterator();		
		
		try {			
			BufferedWriter bufferWriter = new BufferedWriter(new FileWriter(sFileName));
			
			while (true) {
				try {
					String string = iter.next();
					bufferWriter.write(string + "\n");
					
				} catch (NoSuchElementException ex) {
					break;
				}
			}
			
			bufferWriter.flush();
			bufferWriter.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void writeText(String[] data, String sFileName) {
		try {			
			BufferedWriter bufferWriter = new BufferedWriter(new FileWriter(sFileName));
			for (int i = 0; i < data.length; i++) {
				bufferWriter.write(data[i]);
				bufferWriter.newLine();			
			}
			bufferWriter.flush();
			bufferWriter.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void writeConversation(Conversation[] data, String sFileName) {
		try {			
			BufferedWriter bufferWriter = new BufferedWriter(new FileWriter(sFileName));
			for (int i = 0; i < data.length; i++) {
				bufferWriter.write(Integer.toString(i + 1));
				bufferWriter.newLine();			
				bufferWriter.write(data[i].getTopic() + "\n");
				bufferWriter.write(data[i].getQuestion() + "\n");
				bufferWriter.write(data[i].getAnswer() + "\n");
				bufferWriter.write("==================================================\n");
			}
			bufferWriter.flush();
			bufferWriter.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
