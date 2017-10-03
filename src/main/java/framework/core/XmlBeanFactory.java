package framework.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import framework.parsers.Bean;
import framework.parsers.Property;

public class XmlBeanFactory implements BeanFactory {

	private Map<String, Object> objectTable = new HashMap<>();
	private Map<String, Object> interceptorTable = new HashMap<>();

	// Need to keep all beans in order to support different scopes
	private Map<String, Bean> beanTable = new HashMap<>();

	private Stack<Bean> sortedBeans = new Stack<>();
	private List<String> topologicSortVisitedBeanNames = new ArrayList<>();

	private int noNameBeanId = 0;

	public XmlBeanFactory(String xmlFilePath, XmlBeanDefinitionReader xbdr) throws Exception {
		xbdr.loadBeanDefinitions(xmlFilePath);
		topologicalSort(xbdr.getBeanList());
		generateBeans(new ArrayList<>(sortedBeans));
		setupInterceptors(xbdr.getInterceptorList());
	}

	private void topologicalSort(List<Bean> beans) {
		for (Bean bean : beans)
			if (bean.getName() == null || !topologicSortVisitedBeanNames.contains(bean.getName()))
				topologicalSort(bean);
	}

	// Sort dependencies in order to correctly create all beans
	private void topologicalSort(Bean bean) {
		if (bean.getName() == null)
			bean.setName((noNameBeanId++) + "-b");

		topologicSortVisitedBeanNames.add(bean.getName());

		for (Bean son : bean.getConstructorArg())
			if (son.getName() == null || !topologicSortVisitedBeanNames.contains(son.getName()))
				topologicalSort(son);

		for (Property property : bean.getProperties()) {
			Bean son = property.getBean();
			if (son.getName() == null || !topologicSortVisitedBeanNames.contains(son.getName()))
				topologicalSort(son);
		}

		beanTable.put(bean.getName(), bean);
		sortedBeans.push(bean);
	}

	private Object generateBean(Bean parsedBean) throws Exception {
		Class<?> beanClass = calculateClassName(parsedBean.getClassName());
		Constructor<?> beanConstructor;
		Object object;

		List<Bean> constructorBeanArguments = parsedBean.getConstructorArg();

		if (!constructorBeanArguments.isEmpty()) {
			Class<?>[] constructorClasses = constructorBeanArguments.stream().map(constructorParsedBean -> {
				try {
					return calculateClassName(constructorParsedBean.getClassName());
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(e);
				}
			}).toArray(Class<?>[]::new);

			Object[] constructorObjectArguments = constructorBeanArguments.stream().map(t -> {
				try {
					return calculateBean(t);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}).toArray();

			beanConstructor = beanClass.getConstructor(constructorClasses);
			object = beanConstructor.newInstance(constructorObjectArguments);
		} else {
			if (beanClass.isPrimitive()) {
				object = getWrapperClassValueForPrimitiveType(beanClass, parsedBean.getValue());
			} else if (parsedBean.getValue() != null) {
				object = beanClass.cast(parsedBean.getValue());
			} else {
				beanConstructor = beanClass.getConstructor();
				object = beanConstructor.newInstance();
			}
		}

		List<Property> parsedBeanProperties = parsedBean.getProperties();
		if (!parsedBeanProperties.isEmpty()) {
			for (Property parsedBeanProperty : parsedBeanProperties) {
				char first = Character.toUpperCase(parsedBeanProperty.getName().charAt(0));
				String methodName = "set" + first + parsedBeanProperty.getName().substring(1);
				Method method = object.getClass().getMethod(methodName,
						new Class[] { parsedBeanProperty.getBean().getClassName().getClass() });
				method.invoke(object, calculateBean(parsedBeanProperty.getBean()));
			}
		}

		return object;
	}

	private void generateBeans(List<Bean> beanList) throws Exception {
		for (Bean parsedBean : beanList) {
			Object object = generateBean(parsedBean);
			objectTable.put(parsedBean.getName(), object);
		}
	}

	private Class<?> calculateClassName(String name) throws ClassNotFoundException {
		Class<?> beanClass;
		if (name == null || name.equals("String")) {
			beanClass = String.class;
		} else if (classLibrary.containsKey(name)) {
			beanClass = getPrimitiveClassForName(name);
		} else {
			beanClass = Class.forName(name);
		}

		return beanClass;
	}

	private Object calculateBean(Bean bean) throws Exception {
		Class<?> beanClass = calculateClassName(bean.getClassName());
		boolean isPrototype = beanTable.get(bean.getName()).isPrototype();

		if (isPrototype) {
			return generateBean(bean);
		} else {
			Object object = objectTable.get(bean.getName());

			if (object == null) {
				if (beanClass.isPrimitive()) {
					return getWrapperClassValueForPrimitiveType(beanClass, bean.getValue());
				}
				object = beanClass.cast(bean.getValue());
			}

			return object;
		}
	}

	private void setupInterceptors(List<Bean> interceptorList) {
		for (Bean b : interceptorList) {
			try {
				final Class<?> clazz = Class.forName(b.getClassName());
				Object interceptor = clazz.getConstructor().newInstance();
				interceptorTable.put(b.getName(), interceptor);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public Object getBean(String string) throws Exception {
		return calculateBean(beanTable.get(string));
	}

	@SuppressWarnings("unchecked")
	public <T> T getBean(String string, Class<T> type) throws Exception {
		return (T) getBean(string);
	}

	public Object[] getInterceptors() {
		return (Object[]) interceptorTable.values().toArray();
	}

}
