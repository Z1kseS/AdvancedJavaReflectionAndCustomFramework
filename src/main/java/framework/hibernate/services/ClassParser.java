package framework.hibernate.services;

import java.util.List;

public interface ClassParser {
	<T> String getTableName(Class<T> classObject);

	<T> List<String> getTableFieldNames(Class<T> classObject);

	<T> String getIdName(Class<T> classObject);

	<T> long getId(Class<T> classObject, T object) throws Exception;

	<T> long getFkValue(Class<T> classObject, T object, String name) throws Exception;
}
