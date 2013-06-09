package runner;

import generated.Query;

import java.io.InputStream;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class Crawler {
	
//	private static String API_KEY = "dj0yJmk9RzYwQXV0YVJaNkEyJmQ9WVdrOVlrRlpaRGRtTkRnbWNHbzlNVGc1TXpZeU56QTJNZy0tJnM9Y29uc3VtZXJzZWNyZXQmeD0wMg--";
//	private static String API_SECRET = "01d8314e08f1e207370f8b778485bd6af18e34ff";

    public static void main(String[] args) throws Exception {
        JAXBContext jc = JAXBContext.newInstance(Query.class);

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        URL url = new URL("http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20answers.getbycategory%20where%20category_name%3D%22%C4%90i%E1%BB%87n%20t%E1%BB%AD%22%20and%20type%3D%22resolved%22%20and%20region%3D%22vn%22&diagnostics=true");
        InputStream xmlStream = url.openStream();
        
        Query answer = (Query) unmarshaller.unmarshal(xmlStream);
        answer.write("data/raw-conversation.txt");
        
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(answer, System.out);
    }

}
