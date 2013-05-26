package data;

import java.util.LinkedList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement
@XmlSeeAlso(Conversation.class)
public class AimlFile {
	LinkedList<Conversation> aimls;
	
	public AimlFile() {
		aimls = new LinkedList<Conversation>();
	}
	
	@XmlElement(name="aiml")
	public void setAimls(LinkedList<Conversation> aimls_) {
		this.aimls = aimls_;
	}
	
	public LinkedList<Conversation> getAimls() {
		return aimls;
	}
	
	public void addAiml(Conversation aiml) {
		aimls.add(aiml);
	}
}
