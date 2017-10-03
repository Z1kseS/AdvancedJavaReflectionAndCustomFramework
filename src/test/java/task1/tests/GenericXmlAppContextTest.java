package task1.tests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.*;

import application.classes.Bus;
import framework.core.context.GenericXmlApplicationContext;

public class GenericXmlAppContextTest {

	public <T> T getMockObject(Class<T> classToBeMocked) throws Exception {
		return Mockito.mock(classToBeMocked);
	}

	@Test
	public void testCreationDefaultConstructor() {
		GenericXmlApplicationContext tester = new GenericXmlApplicationContext();
		assertNotNull(tester);
	}

	@Test
	public void testCreationStringConstructor() {
		GenericXmlApplicationContext tester = new GenericXmlApplicationContext("filename");
		assertNotNull(tester);
	}

	@Test
	public void testGetBeanFactory() {
		GenericXmlApplicationContext tester = new GenericXmlApplicationContext();
		assertNotNull(tester.getBeanFactory());
	}

	@Test
	public void testGetBean() throws Exception {
		GenericXmlApplicationContext tester = new GenericXmlApplicationContext();
		assertNotNull(tester.getBeanFactory().getBean("bus"));
	}

	@Test
	public void testGetBeanGeneric() throws Exception {
		GenericXmlApplicationContext tester = new GenericXmlApplicationContext();
		assertNotNull((Bus) tester.getBeanFactory().getBean("bus", Bus.class));
	}

	@Test
	public void testSetParserTypeSupported() {
		GenericXmlApplicationContext tester = new GenericXmlApplicationContext();
		assertNotNull(tester);
	}
}
