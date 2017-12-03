package application.hw3;

import framework.core.annotations.Component;
import framework.core.annotations.MessageDriven;
import framework.core.jms.Message;
import framework.core.jms.MessageListener;

@MessageDriven(mappedName = "mn2", name = "msg2")
@Component("msg2")
public class MessageListener2 implements MessageListener {

	@Override
	public void onMessage(Message message) {
		System.out.println("Bonjour: " + message.getMessage().toLowerCase());
	}
}
