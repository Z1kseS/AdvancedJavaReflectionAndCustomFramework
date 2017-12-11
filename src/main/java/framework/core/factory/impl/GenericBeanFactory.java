package framework.core.factory.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import framework.core.factory.BeanFactory;
import framework.core.reader.BeanDefinitionReader;
import framework.parsers.Bean;
import framework.parsers.Property;

public class GenericBeanFactory<T> implements BeanFactory {

	private Map<String, Object> objectTable = new HashMap<>();

	// Need to keep all beans in order to support different scopes
	private Map<String, Bean> beanTable = new HashMap<>();

	private Stack<String> sortedBeans = new Stack<>();
	private List<String> topologicSortVisitedBeanNames = new ArrayList<>();

	private int noNameBeanId = 0;

	public GenericBeanFactory(T provider, BeanDefinitionReader<T> xbdr) throws Exception {
		topologicalSort(xbdr.getBeanList(provider));
		sortedBeans.push("beanFactory");
		Bean beanFactory = new Bean();
		beanFactory.setStub(true);
		beanFactory.setName("beanFactory");
		beanFactory.setClassName("framework.core.factory.BeanFactory");
		beanTable.put("beanFactory", beanFactory);
		generateBeans(new ArrayList<>(sortedBeans));
	}

	private void topologicalSort(List<Bean> beans) {
		for (Bean bean : beans)
			if (bean.getName() == null || !topologicSortVisitedBeanNames.contains(bean.getName()))
				topologicalSort(bean);
	}

	// Sort dependencies in order to correctly create all beans
	private void topologicalSort(Bean bean) {
		if (!bean.isStub()) {
			beanTable.put(bean.getName(), bean);
		} else
			return;

		if (bean.getName() == null)
			bean.setName((noNameBeanId++) + "-b");

		topologicSortVisitedBeanNames.add(bean.getName());

		for (Bean son : bean.getConstructorArg())
			if (son.getName() == null || !son.isStub())
				topologicalSort(son);

		for (Property property : bean.getProperties()) {
			Bean son = property.getBean();
			if (son.getName() == null || !son.isStub())
				topologicalSort(son);
		}

		sortedBeans.push(bean.getName());
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
						new Class[] { Class.forName(parsedBeanProperty.getBean().getClassName()) });
				method.invoke(object, calculateBean(parsedBeanProperty.getBean()));
			}
		}

		return object;
	}

	private void generateBeans(List<String> beanList) throws Exception {
		for (String parsedBeanName : beanList) {
			if (parsedBeanName.equals("beanFactory")) {
				objectTable.put("beanFactory", this);
				continue;
			}
			Object object = generateBean(beanTable.get(parsedBeanName));
			objectTable.put(parsedBeanName, object);
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
		boolean isPrototype = beanTable.get(bean.getName()).isPrototype();

		if (isPrototype) {
			return generateBean(bean);
		} else {
			return objectTable.get(bean.getName());
		}
	}

	public synchronized Object getBean(String string) throws Exception {
		return calculateBean(beanTable.get(string));
	}

	@SuppressWarnings("unchecked")
	public synchronized <K> K getBean(String string, Class<K> type) throws Exception {
		return (K) getBean(string);
	}

	@Override
	public List<Object> getObjectsWithAnnotation(Class<? extends Annotation> annotation) {
		List<Object> objects = new ArrayList<>();
		for (Object object : objectTable.values()) {
			if (object.getClass().isAnnotationPresent(annotation))
				objects.add(object);
		}
		return objects;
	}

}
