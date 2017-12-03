package application.main.lab3;

import application.pw3.CustomSocketClient;
import framework.core.context.GenericAnnotationApplicationContext;

public class ClientMainApp {

	private static GenericAnnotationApplicationContext context = new GenericAnnotationApplicationContext(
			"application.pw3");

	public static void main(String[] args) throws Exception {
		CustomSocketClient customSocketClient = (CustomSocketClient) context.getBeanFactory()
				.getBean("customSocketClient");
		customSocketClient.start("localhost", 8080);
	}
}