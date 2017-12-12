package framework.hibernate.manager.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import framework.core.annotations.Autowiring;
import framework.core.annotations.Component;
import framework.hibernate.annotations.relation.ManyToMany;
import framework.hibernate.annotations.relation.ManyToOne;
import framework.hibernate.annotations.relation.OneToMany;
import framework.hibernate.annotations.relation.OneToOne;
import framework.hibernate.manager.EntityManager;
import framework.hibernate.services.ClassParser;
import framework.hibernate.services.QueryGenerator;
import framework.hibernate.services.ResultSetParser;

@Component(value = "entityManager")
public class EntityManagerImpl implements EntityManager {

	private String dbDriverClass;
	private String dbUrl;
	private String dbUser;
	private String dbPassword;

	@Autowiring
	private QueryGenerator queryGenerator;

	@Autowiring
	private ResultSetParser resultSetParser;

	@Autowiring
	private ClassParser classParser;

	public void loadProperties(String fileName) throws IOException, ClassNotFoundException {
		Properties props = new Properties();
		FileInputStream fis = new FileInputStream(fileName);
		props.load(fis);

		this.dbDriverClass = props.getProperty("DB_DRIVER_CLASS");
		this.dbUser = props.getProperty("DB_USERNAME");
		this.dbPassword = props.getProperty("DB_PASSWORD");
		this.dbUrl = props.getProperty("DB_URL");

		Class.forName(this.dbDriverClass);
	}

	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(this.dbUrl, this.dbUser, this.dbPassword);
	}

	public void setQueryGenerator(QueryGenerator queryGenerator) {
		this.queryGenerator = queryGenerator;
	}

	public void setResultSetParser(ResultSetParser resultSetParser) {
		this.resultSetParser = resultSetParser;
	}

	@Override
	public <T> List<T> getEntities(Class<T> entityClass) throws Exception {
		Connection connection = getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(queryGenerator.getQueryGetAll(entityClass));

		List<T> result = new ArrayList<>();
		while (resultSet.next()) {
			T object = resultSetParser.getObject(entityClass, resultSet);
			applyRelations(entityClass, object, resultSet);
			result.add(object);
		}
		return result;
	}

	@Override
	public <T> T getEntity(Class<T> entityClass, long pk) throws Exception {
		Connection connection = getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(queryGenerator.getEntity(entityClass, pk));

		T result = null;
		while (resultSet.next()) {
			result = resultSetParser.getObject(entityClass, resultSet);
			applyRelations(entityClass, result, resultSet);
		}
		return result;
	}

	@Override
	public <T> List<T> getEntities(Class<T> entityClass, String fkName, long fk) throws Exception {
		Connection connection = getConnection();
		Statement statement = connection.createStatement();
		System.out.println(queryGenerator.getEntities(entityClass, fkName, fk));
		ResultSet resultSet = statement.executeQuery(queryGenerator.getEntities(entityClass, fkName, fk));

		List<T> result = new ArrayList<>();
		while (resultSet.next()) {
			T object = resultSetParser.getObject(entityClass, resultSet);
			applyRelations(entityClass, object, resultSet);
			result.add(object);
		}
		return result;
	}

	private <T> void applyRelations(Class<T> entityClass, T targetObject, ResultSet resultSet) throws Exception {
		for (Field field : entityClass.getDeclaredFields()) {
			if (field.isAnnotationPresent(OneToOne.class)) {
				applyOneToOne(field, entityClass, targetObject);
			} else if (field.isAnnotationPresent(ManyToOne.class)) {
				applyManyToOne(field, entityClass, targetObject, resultSet);
			} else if (field.isAnnotationPresent(OneToMany.class)) {
				applyOneToMany(field, entityClass, targetObject);
			} else if (field.isAnnotationPresent(ManyToMany.class)) {
				applyManyToMany(field, entityClass, targetObject);
			}
		}
	}

	private <T> void applyManyToMany(Field field, Class<T> entityClass, T targetObject) throws Exception {
		ManyToMany annotation = field.getAnnotation(ManyToMany.class);
		String sourceField = annotation.sourceFieldName();
		String targetField = annotation.targetFieldName();
		String table = annotation.table();
		Class<?> targetClass = field.getType();
		List<?> targetObjects = getEntities(targetClass, table, sourceField, targetField,
				classParser.getId(entityClass, targetObject));

		setValue(field.getName(), targetClass, targetObject, targetObjects);
	}

	private <T> void applyOneToMany(Field field, Class<T> entityClass, T targetObject) throws Exception {
		OneToMany annotation = field.getAnnotation(OneToMany.class);
		String fkName = annotation.fieldName();
		Class<?> targetClass = field.getType();
		List<?> targetObjects = getEntities(targetClass, fkName, classParser.getId(entityClass, targetObject));
		setValue(field.getName(), targetClass, targetObject, targetObjects);
	}

	private <T> void applyManyToOne(Field field, Class<T> entityClass, T targetObject, ResultSet resultSet)
			throws Exception {
		ManyToOne annotation = field.getAnnotation(ManyToOne.class);
		String fkName = annotation.fieldName();
		Class<?> targetClass = field.getType();

		List<?> targetObjects = getEntities(targetClass, fkName, resultSet.getLong(fkName));
		setValue(field.getName(), targetClass, targetObject, (targetObjects.size() == 1) ? targetObjects.get(0) : null);
	}

	private <T> void applyOneToOne(Field field, Class<T> entityClass, T targetObject) throws Exception {
		OneToOne annotation = field.getAnnotation(OneToOne.class);
		String fkName = annotation.fieldName();
		Class<?> targetClass = field.getType();
		if (annotation.thisSide()) {
			List<?> targetObjects = getEntities(targetClass, fkName, classParser.getId(entityClass, targetObject));
			setValue(field.getName(), targetClass, targetObject,
					(targetObjects.size() == 1) ? targetObjects.get(0) : null);
		} else {
			List<?> targetObjects = getEntities(targetClass, fkName,
					classParser.getFkValue(entityClass, targetObject, fkName));
			setValue(field.getName(), targetClass, targetObject,
					(targetObjects.size() == 1) ? targetObjects.get(0) : null);
		}
	}

	private void setValue(String fieldName, Class<?> className, Object targetObject, Object propertyObject)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		char first = Character.toUpperCase(fieldName.charAt(0));
		String methodName = "set" + first + fieldName.substring(1);
		Method method = targetObject.getClass().getMethod(methodName, new Class[] { className });
		method.invoke(targetObject, propertyObject);
	}

	@Override
	public <T> List<T> getEntities(Class<T> entityClass, String fkTable, String pkName, String fkName, long fk)
			throws Exception {
		Connection connection = getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement
				.executeQuery(queryGenerator.getEntities(entityClass, fkTable, pkName, fkName, fk));

		List<T> result = new ArrayList<>();
		while (resultSet.next()) {
			T object = resultSetParser.getObject(entityClass, resultSet);
			applyRelations(entityClass, object, resultSet);
			result.add(object);
		}
		return result;
	}

	public void setClassParser(ClassParser classParser) {
		this.classParser = classParser;
	}
}
