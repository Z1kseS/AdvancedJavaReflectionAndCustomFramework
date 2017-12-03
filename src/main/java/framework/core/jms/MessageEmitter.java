package framework.core.jms;

import java.util.List;
import java.util.stream.Collectors;

import framework.core.annotations.Autowiring;
import framework.core.annotations.Component;
import framework.core.annotations.MessageDriven;
import framework.core.factory.BeanFactory;

@Component("messageEmitter")
public class MessageEmitter {

	@Autowiring
	private BeanFactory beanFactory;

	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	public void emitMessage(String target, Message message) {
		List<Object> objects = beanFactory.getObjectsWithAnnotation(MessageDriven.class);

		objects = objects.stream()
				.filter(obj -> obj.getClass().isAnnotationPresent(MessageDriven.class)
						&& obj.getClass().getAnnotation(MessageDriven.class).mappedName().equals(target))
				.collect(Collectors.toList());

		for (Object object : objects) {
			((MessageListener) object).onMessage(message);
		}
	}
}
