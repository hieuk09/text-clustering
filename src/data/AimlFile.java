package data;

import java.util.LinkedList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement
@XmlSeeAlso(Aiml.class)
public class AimlFile {
	LinkedList<Aiml> aimls;
	
	public AimlFile() {
		aimls = new LinkedList<Aiml>();
	}
	
	@XmlElement(name="aiml")
	public void setAimls(LinkedList<Aiml> aimls_) {
		this.aimls = aimls_;
	}
	
	public LinkedList<Aiml> getAimls() {
		return aimls;
	}
	
	public void addAiml(Aiml aiml) {
		aimls.add(aiml);
	}
}
