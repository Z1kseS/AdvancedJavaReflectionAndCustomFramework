package framework.parsers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;

import framework.core.annotations.Autowiring;
import framework.core.annotations.Component;
import framework.core.annotations.Prototype;
import framework.core.annotations.Statefull;

public class PackageParser implements Parser {

	private String packageName;

	public PackageParser(String packageName) {
		this.packageName = packageName;
	}

	@Override
	public List<Bean> getBeanList() {
		List<Bean> beans = new ArrayList<>();
		beans.addAll(calculateBeanList(this.packageName));
		beans.addAll(calculateBeanList("framework"));

		return beans;
	}

	private List<Bean> calculateBeanList(String packageName) {
		List<Bean> beans = new ArrayList<>();
		Reflections reflections = new Reflections(packageName);
		Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Component.class);
		for (Class<?> currentClass : classes) {
			Bean bean = new Bean();
			String id = currentClass.getAnnotation(Component.class).value();

			for (Bean b : beans)
				if (b.getName().equals(id))
					bean = b;

			bean.setName(id);
			bean.setClassName(currentClass.getName());
			bean.setStub(false);
			bean.setPrototype(
					getClass().isAnnotationPresent(Prototype.class) || currentClass.isAnnotationPresent(Statefull.class)
							? "prototype" : "singleton");
			for (Field currentField : currentClass.getDeclaredFields()) {
				if (currentField.isAnnotationPresent(Autowiring.class)) {
					Property property = new Property();
					property.setName(currentField.getName());
					Bean stubBean = new Bean();
					stubBean.setStub(true);
					stubBean.setName(currentField.getName());
					stubBean.setClassName(currentField.getType().getName());
					property.setBean(stubBean);

					bean.getProperties().add(property);
				}
			}

			beans.add(bean);
		}

		return beans;
	}

}
