package processor;

import vn.hus.nlp.tokenizer.VietTokenizer;

public class TextTokenizer {
	
	static VietTokenizer processor = new VietTokenizer();;
	public static String EMAIL_PATTERN = "([^.@\\s]+)(\\.[^.@\\s]+)*@([^.@\\s]+\\.)+([^.@\\s]+)";
	
	public static String[] tokenize(String text) {
		try {
			return processor.tokenize(removeEmail(text));
		} catch (Exception e) {
			System.out.println(text);
			e.printStackTrace();
			return null;
		}
		 
	}
	
	public static String removeEmail(String text) {
		return text.replaceAll(EMAIL_PATTERN, "").replace('@', '_');
	}
	
}
