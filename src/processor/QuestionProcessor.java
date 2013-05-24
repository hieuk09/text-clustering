package processor;

import input.Input;

import java.util.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;

import org.w3c.dom.*;
import java.io.*;

import svm.SvmProcessor;

import jvntagger.MaxentTagger;
import jvntagger.POSTagger;

public class QuestionProcessor {
	
	public static String getTrueQuestion(String question) {
		//initialize data
//		ArrayList<Object> dictionary = Input.readFromText("data/dictionary.txt");
//		ArrayList<Object> topics = Input.readFromText("data/topic.txt");
//		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
//		String modelDir = "model/maxent";
//		POSTagger tagger = new MaxentTagger(modelDir);;
//		
//		//convert question -> svm feature
//		//tokenize string		
//		String[] tokens = TextTokenizer.tokenize(question);
//		question = Utility.joinString(tokens, ' ');
//		//remove stopword
//		question = tagger.tagging(question);
//		question = Utility.replaceUnwantedWord(question, "");
//		//convert to feature
//		String[] strings = question.split(" ");
//		Utility.putIndex2Map(strings, map, dictionary);
//		
//		Collection<Integer> unsorted = map.keySet();
//		List<Integer> sorted = asSortedList(unsorted);
//		Iterator<Integer> iter = sorted.iterator();
//		String feature = "1";
//		while (true) {
//			try {
//				Integer index = (Integer)iter.next();
//				Double value = (double) (map.get(index)*1.0 / (strings.length));
//				//write to buffer
//				feature += " " + index.toString() + ":" + value.toString();
//
//			} catch (NoSuchElementException ex) {
//				break;
//			}
//		}
//		
//		String outputLabel = "1";
//		
//		//predict using svm		
//		//SvmProcessor.predict(input, output, model, predict_probability);
//		try {
//			outputLabel = SvmProcessor.svmPredict(feature);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		//convert svm label to topic
//		//get label from output		
//		Double temp = Double.parseDouble(outputLabel);		
//		Integer outputLabelNumber = temp.intValue();
//		String label = (String)topics.get(outputLabelNumber);
//		System.out.println(label);
		
		//from topic, get the question for AIML module
		InputStream bais = null;
		try {
            //bais = new ByteArrayInputStream(xml.getBytes());
			bais = new FileInputStream("data/all.xml");			 
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document xmlDoc = builder.parse(bais);

            Node root = xmlDoc.getDocumentElement();

            XPathFactory xFactory = XPathFactory.newInstance();
            XPath xPath = xFactory.newXPath();

            XPathExpression xExpress = xPath.compile("/name[@text='tư vấn']");
            NodeList nodes = (NodeList) xExpress.evaluate(root, XPathConstants.NODESET);

            System.out.println("Found " + nodes.getLength() + " note nodes");

            for (int index = 0; index < nodes.getLength(); index++) {
//                System.out.println(toString(childDoc));
            }

        } catch (Exception exp) {
            exp.printStackTrace();
        } finally {
            try {
                bais.close();
            } catch (Exception e) {
            }
        }
		
		return "Tôi không biết.";
	}
	
	public static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
		List<T> list = new ArrayList<T>(c);
		java.util.Collections.sort(list);
		return list;
	}
}
