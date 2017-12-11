package framework.hibernate.services;

import java.sql.ResultSet;

public interface ResultSetParser {
	<T> T getObject(Class<T> classObject, ResultSet resultSet) throws Exception;
}
