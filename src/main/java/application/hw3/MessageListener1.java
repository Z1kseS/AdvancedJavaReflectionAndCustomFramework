package application.hw3;

import framework.core.annotations.Component;
import framework.core.annotations.MessageDriven;
import framework.core.jms.Message;
import framework.core.jms.MessageListener;

@MessageDriven(mappedName = "mn1", name = "msg1")
@Component("msg1")
public class MessageListener1 implements MessageListener {

	@Override
	public void onMessage(Message message) {
		System.out.println("Alloha: " + message.getMessage().toUpperCase());
	}
}
