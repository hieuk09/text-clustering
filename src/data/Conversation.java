package data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Conversation {
	private String question;
	private String answer;
	private String topic;
	
	public Conversation() {
	}
	
	public Conversation(String question, String answer, String topic) {
		this.question = question;
		this.answer = answer;
		this.topic = topic;
	}

	public String getQuestion() {
		return question;
	}

	@XmlElement
	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	@XmlElement
	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getTopic() {
		return topic;
	}

	@XmlElement
	public void setTopic(String topic) {
		this.topic = topic;
	}
}
