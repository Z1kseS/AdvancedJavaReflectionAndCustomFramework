package framework.hibernate.services.impl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import framework.core.annotations.Component;
import framework.hibernate.annotations.Bool;
import framework.hibernate.annotations.DateTime;
import framework.hibernate.annotations.Floating;
import framework.hibernate.annotations.Id;
import framework.hibernate.annotations.Int;
import framework.hibernate.annotations.Text;
import framework.hibernate.services.ResultSetParser;

@Component("resultSetParser")
public class ResultSetParserImpl implements ResultSetParser {

	@Override
	public <T> T getObject(Class<T> classObject, ResultSet resultSet) throws Exception {
		T object = classObject.newInstance();

		for (Field field : classObject.getDeclaredFields())
			setFieldValue(object, field, resultSet);
		return object;
	}

	private void setFieldValue(Object targetObject, Field field, ResultSet resultSet)
			throws SQLException, NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Object value = null;
		Class<?> classObject = null;
		if (field.getAnnotation(Text.class) != null) {
			value = resultSet.getString(field.getAnnotation(Text.class).fieldName());
			classObject = String.class;
		} else if (field.getAnnotation(Int.class) != null) {
			value = resultSet.getInt(field.getAnnotation(Int.class).fieldName());
			classObject = Integer.class;
		} else if (field.getAnnotation(Floating.class) != null) {
			value = resultSet.getFloat(field.getAnnotation(Floating.class).fieldName());
			classObject = Float.class;
		} else if (field.getAnnotation(Bool.class) != null) {
			value = resultSet.getBoolean(field.getAnnotation(Bool.class).fieldName());
			classObject = Boolean.class;
		} else if (field.getAnnotation(DateTime.class) != null) {
			value = resultSet.getDate(field.getAnnotation(DateTime.class).fieldName());
			classObject = Date.class;
		} else if (field.getAnnotation(Id.class) != null) {
			value = resultSet.getLong(field.getAnnotation(Id.class).fieldName());
			classObject = long.class;
		}

		if (classObject == null)
			return;

		setValue(field.getName(), classObject, targetObject, value);
	}

	private void setValue(String fieldName, Class<?> className, Object targetObject, Object propertyObject)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		char first = Character.toUpperCase(fieldName.charAt(0));
		String methodName = "set" + first + fieldName.substring(1);
		Method method = targetObject.getClass().getMethod(methodName, new Class[] { className });
		method.invoke(targetObject, propertyObject);
	}

}
