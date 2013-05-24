package data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement
@XmlSeeAlso(RandomTag.class)
public class TemplateTag extends Tag {
	Tag random;
	
	public TemplateTag() {
		random = new RandomTag();
	}
	
	public TemplateTag(String string) {
		random = new RandomTag(string);
	}
	
	public Tag getRandom() {
		return random;
	}
	
	@XmlElement
	public void setRandom(Tag randomTag_) {
		random = randomTag_;
	}
}
