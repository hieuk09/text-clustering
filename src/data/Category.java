package data;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlSeeAlso(TemplateTag.class)
public class Category {
	String pattern;
	TemplateTag template;
	
	public Category() {
		pattern = "";
		template = new TemplateTag();
	}
	
	public Category(Conversation conv) {
		pattern = conv.getQuestion().toUpperCase();
		template = new TemplateTag(conv.getAnswer());
	}
 
	public String getPattern() {
		return pattern;
	}
 
	@XmlElement
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
 
	public TemplateTag getTemplate() {
		return template;
	}
 
	@XmlElement
	public void setTemplate(TemplateTag template) {
		this.template = template;
	}
	
	public void addTemplate(String template, String type) {
		RandomTag randomTag = (RandomTag)this.template.getRandom();
		if (type == "li") {
			randomTag.addLiTag(template);
		} else if (type == "srai") {
			randomTag.addSraiTag(template);
		}
	}
}
