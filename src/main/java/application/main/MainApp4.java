package application.main;

import application.pw4.Student;
import framework.core.context.GenericAnnotationApplicationContext;
import framework.hibernate.manager.EntityManager;

public class MainApp4 {

	private static GenericAnnotationApplicationContext context = new GenericAnnotationApplicationContext(
			"application.classes.annotations");

	public static void main(String[] args) throws Exception {
		EntityManager entityManager = (EntityManager) context.getBeanFactory().getBean("entityManager");
		entityManager.loadProperties(
				"C:\\Users\\Oleh Yanivskyy\\Desktop\\Магістратура\\БВПМ\\AdvancedJavaReflectionAndCustomFramework\\src\\main\\resources\\db.properties");

		System.out.println(entityManager.getEntities(Student.class));
	}
}