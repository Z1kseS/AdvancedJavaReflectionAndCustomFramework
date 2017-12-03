package application.main.lab3;

import application.lab3.CustomSocketServer;
import framework.core.context.GenericAnnotationApplicationContext;

public class ServerMainApp {

	private static GenericAnnotationApplicationContext context = new GenericAnnotationApplicationContext(
			"application.lab3");

	public static void main(String[] args) throws Exception {
		CustomSocketServer customServerSocket = (CustomSocketServer) context.getBeanFactory()
				.getBean("customSocketServer");
		customServerSocket.start();
	}
}