package framework.hibernate.services.impl;

import java.util.List;

import framework.core.annotations.Autowiring;
import framework.core.annotations.Component;
import framework.hibernate.services.ClassParser;
import framework.hibernate.services.QueryGenerator;

@Component(value = "queryGenerator")
public class QueryGeneratorImpl implements QueryGenerator {

	@Autowiring
	private ClassParser classParser;

	@Override
	public <T> String getQueryGetAll(Class<T> classObject) {
		List<String> names = classParser.getTableFieldNames(classObject);
		String tableName = classParser.getTableName(classObject);

		String query = "select ";

		for (String fieldName : names)
			query += fieldName + ", ";

		query = query.substring(0, query.length() - 2);
		query += " from " + tableName;
		return query;
	}

	@Override
	public <T> String getEntity(Class<T> entityClass, long pk) {
		String baseSql = getQueryGetAll(entityClass);
		String idName = classParser.getIdName(entityClass);

		baseSql += " where " + idName + "=" + pk;
		return baseSql;
	}

	@Override
	public <T> String getEntities(Class<T> entityClass, String fkName, long fk) {
		return getQueryGetAll(entityClass) + " where " + fkName + "=" + fk;
	}

	@Override
	public <T> String getEntities(Class<T> entityClass, String fkTable, String pkName, String fkName, long fk) {
		String baseSql = getQueryGetAll(entityClass);
		baseSql += ", " + fkTable;
		baseSql += " where " + fkName + "=" + pkName + " AND " + fkName + "=" + fk;
		return baseSql;
	}

	public void setClassParser(ClassParser classParser) {
		this.classParser = classParser;
	}

}
