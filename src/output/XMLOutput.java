package output;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.*;

import org.w3c.dom.*;

import data.Aiml;
import data.AimlFile;

public class XMLOutput{
	DocumentBuilderFactory docFactory = null;
	DocumentBuilder docBuilder = null;
	Document doc = null;
	
	public XMLOutput()
	{
		try {
			docFactory = DocumentBuilderFactory.newInstance();
			docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.newDocument();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	public void write(String[] data) {
		// TODO Auto-generated method stub
		Element rootElement = doc.createElement("AIML");
		doc.appendChild(rootElement);
		for (int i = 0; i < data.length; i++)
		{
			// staff elements
			Element category = doc.createElement("Category");
			category.appendChild(doc.createTextNode(data[i]));
			rootElement.appendChild(category);
		}
	}
	
	public static boolean save(String fileName, Aiml aiml)
	{	
		try {

			File file = new File(fileName);
			JAXBContext jaxbContext = JAXBContext.newInstance(Aiml.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			jaxbMarshaller.marshal(aiml, file);

		} catch (JAXBException e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}
	
	public static boolean saveConversation(String fileName, AimlFile aiml)
	{	
		try {

			File file = new File(fileName);
			JAXBContext jaxbContext = JAXBContext.newInstance(AimlFile.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			jaxbMarshaller.marshal(aiml, file);
			//jaxbMarshaller.marshal(aiml, System.out);

		} catch (JAXBException e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

}
