package application.main.lab3;

import application.hw3.StatefullCounter;
import application.hw3.StatelessCounter;
import framework.core.context.GenericWebAnnotationApplicationContext;
import framework.core.jms.Message;
import framework.core.jms.MessageEmitter;

public class StateMainApp {

	private static GenericWebAnnotationApplicationContext context = new GenericWebAnnotationApplicationContext(
			"application.hw3");

	public static void main(String[] args) throws Exception {
		StatefullCounter sfc1 = context.getBean("sid1", "statefullCounter", StatefullCounter.class);
		StatefullCounter sfc2 = context.getBean("sid2", "statefullCounter", StatefullCounter.class);
		StatefullCounter sfc3 = context.getBean("sid1", "statefullCounter", StatefullCounter.class);
		System.out.println(System.identityHashCode(sfc1));
		System.out.println(System.identityHashCode(sfc2));
		System.out.println(System.identityHashCode(sfc3));

		StatelessCounter slc1 = context.getBean("", "statelessCounter", StatelessCounter.class);
		StatelessCounter slc2 = context.getBean("", "statelessCounter", StatelessCounter.class);

		System.out.println(System.identityHashCode(slc1));
		System.out.println(System.identityHashCode(slc2));

		MessageEmitter messageEmitter = context.getBean("", "messageEmitter", MessageEmitter.class);
		messageEmitter.emitMessage("mn1", new Message("blabla", null));
		messageEmitter.emitMessage("mn2", new Message("albalb", null));
	}
}