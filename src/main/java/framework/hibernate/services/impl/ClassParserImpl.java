package framework.hibernate.services.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import framework.core.annotations.Component;
import framework.hibernate.annotations.Bool;
import framework.hibernate.annotations.DateTime;
import framework.hibernate.annotations.Floating;
import framework.hibernate.annotations.Id;
import framework.hibernate.annotations.Int;
import framework.hibernate.annotations.Table;
import framework.hibernate.annotations.Text;
import framework.hibernate.annotations.relation.ManyToOne;
import framework.hibernate.annotations.relation.OneToOne;
import framework.hibernate.services.ClassParser;

@Component("classParser")
public class ClassParserImpl implements ClassParser {
	public <T> String getTableName(Class<T> classObject) {
		if (!classObject.isAnnotationPresent(Table.class))
			throw new IllegalArgumentException("Goal object is not a table");

		return classObject.getAnnotation(Table.class).tableName();
	}

	public <T> List<String> getTableFieldNames(Class<T> classObject) {
		List<String> names = new ArrayList<>();
		for (Field field : classObject.getDeclaredFields()) {
			String fieldName = getFieldName(field);
			if (fieldName != null)
				names.add(fieldName);
		}

		if (names.size() == 0)
			throw new IllegalArgumentException("Invalid number of table fields");

		return names;
	}

	private static String getFieldName(Field field) {
		if (field.getAnnotation(Text.class) != null)
			return field.getAnnotation(Text.class).fieldName();
		else if (field.getAnnotation(Int.class) != null)
			return field.getAnnotation(Int.class).fieldName();
		else if (field.getAnnotation(Floating.class) != null)
			return field.getAnnotation(Floating.class).fieldName();
		else if (field.getAnnotation(Bool.class) != null)
			return field.getAnnotation(Bool.class).fieldName();
		else if (field.getAnnotation(DateTime.class) != null)
			return field.getAnnotation(DateTime.class).fieldName();
		else if (field.getAnnotation(Id.class) != null)
			return field.getAnnotation(Id.class).fieldName();
		else if (field.getAnnotation(ManyToOne.class) != null)
			return field.getAnnotation(ManyToOne.class).fieldName();
		else if (field.getAnnotation(OneToOne.class) != null && field.getAnnotation(OneToOne.class).thisSide())
			return field.getAnnotation(OneToOne.class).fieldName();

		return null;
	}

	@Override
	public <T> String getIdName(Class<T> classObject) {
		for (Field field : classObject.getDeclaredFields())
			if (field.isAnnotationPresent(Id.class))
				return field.getAnnotation(Id.class).fieldName();
		return null;
	}

	@Override
	public <T> long getId(Class<T> classObject, T object) throws Exception {
		String name = null;
		for (Field field : classObject.getDeclaredFields())
			if (field.isAnnotationPresent(Id.class))
				name = field.getAnnotation(Id.class).fieldName();

		if (name == null)
			throw new IllegalArgumentException("Id isn't present");

		char first = Character.toUpperCase(name.charAt(0));
		String methodName = "get" + first + name.substring(1);
		Method method = object.getClass().getMethod(methodName, new Class[] {});
		return (long) method.invoke(object);
	}

	@Override
	public <T> long getFkValue(Class<T> classObject, T object, String name) throws Exception {
		char first = Character.toUpperCase(name.charAt(0));
		String methodName = "get" + first + name.substring(1);
		Method method = object.getClass().getMethod(methodName, new Class[] {});
		return (long) method.invoke(object);
	}
}
