package runner;

import generated.Query;

import input.Input;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class Crawler {

	//	private static String API_KEY = "dj0yJmk9RzYwQXV0YVJaNkEyJmQ9WVdrOVlrRlpaRGRtTkRnbWNHbzlNVGc1TXpZeU56QTJNZy0tJnM9Y29uc3VtZXJzZWNyZXQmeD0wMg--";
	//	private static String API_SECRET = "01d8314e08f1e207370f8b778485bd6af18e34ff";

	public static void main(String[] args) throws Exception {
		getYahooData();
//		getXMLData();
	}

	public static void getYahooData() throws Exception {
		JAXBContext jc = JAXBContext.newInstance(Query.class);

		Unmarshaller unmarshaller = jc.createUnmarshaller();        

		ArrayList<Object> list = Input.readFromText("data/topic.txt");
		Object[] objectArray = list.toArray();
		String[] categories = Arrays.copyOf(objectArray, objectArray.length, String[].class);

		String before = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20answers.search(0%2C50)%20where%20query%3D%22";
		String after = "%22%20and%20type%3D%22resolved%22%20and%20region%3D%22vn%22";

		Query result = null;

		try {
			for (int i = 0; i < categories.length; i++) {	

				System.out.println(Integer.toString(i) + categories[i]);

				URL url = new URL(before + categories[i].replaceAll("[ _]", "%20") + after);

				InputStream xmlStream = url.openStream();

				Query answer = (Query) unmarshaller.unmarshal(xmlStream);

				if (result == null) {
					result = answer;
				} else {
					result.getResults().getQuestion().addAll(answer.getResults().getQuestion());
				}

				File file = new File("data/XML/" + Integer.toString(i) + ".xml");
				Marshaller marshaller = jc.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				marshaller.marshal(answer, file);
			}
		} catch (Exception ex){			
		}

		result.write("data/raw-conversation.txt");
	}

	public static void getXMLData() throws Exception {

		JAXBContext jc = JAXBContext.newInstance(Query.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller(); 
		
		File folder = new File("data/XML");
		Query result = null;

		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				//listFilesForFolder(fileEntry);
			} else {
				System.out.println(fileEntry.getName());
				InputStream xmlStream= new FileInputStream(fileEntry);

				Query answer = (Query) unmarshaller.unmarshal(xmlStream);
				
				if (result == null) {
					result = answer;
				} else {
					result.getResults().getQuestion().addAll(answer.getResults().getQuestion());
				}
			}
		}
		result.write("data/raw-conversation.txt");
	}

}
