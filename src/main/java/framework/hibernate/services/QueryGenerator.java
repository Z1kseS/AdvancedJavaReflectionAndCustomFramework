package framework.hibernate.services;

public interface QueryGenerator {
	<T> String getQueryGetAll(Class<T> classObject);
}
