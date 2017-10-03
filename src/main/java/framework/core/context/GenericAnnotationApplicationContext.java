package framework.core.context;

import framework.core.factory.BeanFactory;
import framework.core.factory.impl.GenericBeanFactory;
import framework.core.reader.impl.AnnotationBeanDefinitionReader;

public class GenericAnnotationApplicationContext {

	private AnnotationBeanDefinitionReader reader;
	private BeanFactory beanFactory;

	private String packageName;

	public GenericAnnotationApplicationContext(String packageName) {
		this.packageName = packageName;
		reader = new AnnotationBeanDefinitionReader();
		try {
			beanFactory = new GenericBeanFactory<String>(this.packageName, reader);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public BeanFactory getBeanFactory() {
		return beanFactory;
	}
}
