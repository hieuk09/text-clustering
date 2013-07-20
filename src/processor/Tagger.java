package processor;

import jvntagger.MaxentTagger;
import jvntagger.POSTagger;

public class Tagger {
	static String modelDir = "model/maxent";
	static POSTagger tagger = new MaxentTagger(modelDir);
	
	public static String tagging(String s) {
		return tagger.tagging(s);
	}
}
