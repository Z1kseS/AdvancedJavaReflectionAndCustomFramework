package framework.hibernate.services.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import framework.core.annotations.Component;
import framework.hibernate.annotations.Bool;
import framework.hibernate.annotations.DateTime;
import framework.hibernate.annotations.Floating;
import framework.hibernate.annotations.Int;
import framework.hibernate.annotations.Table;
import framework.hibernate.annotations.Text;
import framework.hibernate.services.QueryGenerator;

@Component(value = "queryGenerator")
public class QueryGeneratorImpl implements QueryGenerator {

	@Override
	public <T> String getQueryGetAll(Class<T> classObject) {
		List<String> names = new ArrayList<>();
		for (Field field : classObject.getDeclaredFields()) {
			String fieldName = getFieldName(field);
			if (fieldName != null)
				names.add(fieldName);
		}

		if (names.size() == 0)
			throw new IllegalArgumentException("Invalid number of table fields");
		if (!classObject.isAnnotationPresent(Table.class))
			throw new IllegalArgumentException("Goal object is not a table");

		String tableName = classObject.getAnnotation(Table.class).tableName();

		String query = "select ";

		for (String fieldName : names)
			query += fieldName + ", ";

		query = query.substring(0, query.length() - 2);
		query += " from " + tableName;
		return query;
	}

	private String getFieldName(Field field) {
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

		return null;
	}

}
