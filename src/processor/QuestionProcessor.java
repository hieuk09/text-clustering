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
	
	//initialize data
	private static ArrayList<Object> dictionary = Input.readFromText("data/dictionary.txt");
	private static ArrayList<Object> topics = Input.readFromText("data/topic.txt");
	private static POSTagger tagger = new MaxentTagger("model/maxent");
	private static Document xmlDoc = initAiml();
	
	public static String getTrueQuestion(String question) {
		
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();		
				
		String[] tokens = TextTokenizer.tokenize(question);
		question = Utility.joinString(tokens, ' ');

		question = tagger.tagging(question);
		question = Utility.replaceUnwantedWord(question, "");

		String[] strings = question.split(" ");
		Utility.putIndex2Map(strings, map, dictionary);
		
		Collection<Integer> unsorted = map.keySet();
		List<Integer> sorted = asSortedList(unsorted);
		Iterator<Integer> iter = sorted.iterator();
		String feature = "1"; //dummy label
		while (true) {
			try {
				Integer index = (Integer)iter.next();
				Double value = (double) (map.get(index)*1.0 / (strings.length));
				feature += " " + index.toString() + ":" + value.toString();
			} catch (NoSuchElementException ex) {
				break;
			}
		}
		
		String outputLabel = "1"; //dummy label
		
		try {
			outputLabel = SvmProcessor.svmPredict(feature);
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		Double temp = Double.parseDouble(outputLabel);		
		Integer outputLabelNumber = temp.intValue();
		String label = (String)topics.get(outputLabelNumber);
		label = label.replace('_', ' ');
		
		//from topic, get the question for AIML module
		Node root = xmlDoc.getDocumentElement();

        XPathFactory xFactory = XPathFactory.newInstance();
        XPath xPath = xFactory.newXPath();

        XPathExpression xExpress;
        NodeList nodes = null;
		try {
			xExpress = xPath.compile("/aimlFile/aiml/topic[text()='" + label + "']");
			nodes = (NodeList) xExpress.evaluate(root, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        

        int length = nodes.getLength();
        
        if (length > 0) {
        	int i = Utility.random(length);
        	Node node = nodes.item(i);
        	Node parent = node.getParentNode();
    		Node answer = parent.getFirstChild().getNextSibling();
        	return answer.getTextContent();
        }
		
		return "Tôi không biết.";
	}
	
	public static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
		List<T> list = new ArrayList<T>(c);
		java.util.Collections.sort(list);
		return list;
	}
	
	public static Document initAiml() {
		InputStream bais = null;
		Document xmlDoc = null;
		try {
			bais = new FileInputStream("data/all.xml");			 
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            xmlDoc = builder.parse(bais);
        } catch (Exception exp) {
            exp.printStackTrace();
        } finally {
            try {
                bais.close();
            } catch (Exception e) {
            }
        }
		return xmlDoc;
	}
}
