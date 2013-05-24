package data;

import java.util.LinkedList;

import javax.xml.bind.annotation.*;

import processor.Utility;

@XmlRootElement
@XmlSeeAlso(Category.class)
public class Aiml {
	LinkedList<Category> dialogs;
	String name;
	
	public Aiml() {
		name = "";
		dialogs = new LinkedList<Category>();
	}
	
	public Aiml(Category dialog, String topic) {
		dialogs = new LinkedList<Category>();
		dialogs.add(dialog);
		name = topic;
	}
	
	public LinkedList<Category> getDialogs() {
		return dialogs;
	}
	
	@XmlElement(name="category")
	public void setDialogs(LinkedList<Category> category_) {
		this.dialogs = category_;
	}
 
	public String getName() {
		return name;
	}
 
	public void setName(String name) {
		this.name = name;
	}
	
	public void addCategory(Category category_) {
		this.dialogs.add(category_);
	}
	
	public void addCategory(Conversation conv) {
		for (Category dialog : dialogs) {
			String question = conv.getQuestion().toUpperCase();
			if (dialog.pattern.equals(question)) {
				dialog.addTemplate(conv.getAnswer(), "li");
				break;
			} else if (Utility.isSimilar(dialog.pattern, question)) {
				dialog.addTemplate(conv.getQuestion(), "srai");
				Category cat = new Category(conv);
				this.addCategory(cat);
				break;
			}
		}
	}
}
