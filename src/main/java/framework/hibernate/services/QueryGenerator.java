package framework.hibernate.services;

public interface QueryGenerator {
	<T> String getQueryGetAll(Class<T> classObject);

	<T> String getEntity(Class<T> entityClass, long pk);

	<T> String getEntities(Class<T> entityClass, String fkName, long fk);

	<T> String getEntities(Class<T> entityClass, String fkTable, String pkName, String fkName, long fk);
}
