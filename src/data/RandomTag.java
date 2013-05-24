package data;

import java.util.LinkedList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RandomTag extends Tag {
	LinkedList<String> li;
	LinkedList<String> srai;
	
	public RandomTag() {
		li = null;
		srai = null;
	}
	
	public RandomTag(String string) {
		li = null;
		srai = null;				
		
		if (string.contains("*")) {
			srai = new LinkedList<String>();
			srai.add(string);
		} else {
			li = new LinkedList<String>();
			li.add(string);
		}
	}
	
	public LinkedList<String> getLi() {
		return li;
	}
	
	@XmlElement
	public void setLi(LinkedList<String> liTags_) {
		li = liTags_;
	}
	
	public LinkedList<String> getSrai() {
		return srai;
	}
	
	@XmlElementWrapper(name="li")
	public void setSrai(LinkedList<String> sraiTags_) {
		srai = sraiTags_;
	}
	
	public boolean addLiTag(String string) {
		return addTag(li, string);
	}
	
	public boolean addSraiTag(String string) {
		return addTag(srai, string);
	}
	
	public boolean addTag(LinkedList<String> list, String string) {
		if (list == null) {
			list = new LinkedList<String>(); 
		}
		for (String tag : list) {
			if (tag.equals(string)) {
				return false;
			}
		}
		
		list.add(string);
		return true;
	}
}
