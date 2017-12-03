package framework.core.jms;

import java.util.Map;

public class Message {
	private String message;
	private Map<String, Object> properties;

	public Message(String message, Map<String, Object> properties) {
		this.message = message;
		this.properties = properties;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	@Override
	public String toString() {
		return "Message [message=" + message + ", properties=" + properties + "]";
	}

}
