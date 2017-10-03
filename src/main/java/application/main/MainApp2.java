package application.main;

import framework.core.context.GenericAnnotationApplicationContext;

public class MainApp2 {

	private static GenericAnnotationApplicationContext context = new GenericAnnotationApplicationContext(
			"application.classes.annotations");

	public static void main(String[] args) throws Exception {
		System.out.println(context.getBeanFactory().getBean("kitchen"));
	}
}