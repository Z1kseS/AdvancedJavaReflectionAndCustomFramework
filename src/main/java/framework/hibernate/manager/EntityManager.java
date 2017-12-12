package framework.hibernate.manager;

import java.io.IOException;
import java.util.List;

public interface EntityManager {
	<T> List<T> getEntities(Class<T> entityClass) throws Exception;

	<T> T getEntity(Class<T> entityClass, long pk) throws Exception;

	<T> List<T> getEntities(Class<T> entityClass, String fkName, long fk) throws Exception;

	void loadProperties(String fileName) throws IOException, ClassNotFoundException;

	<T> List<T> getEntities(Class<T> entityClass, String fkTable, String pkName, String fkName, long fk) throws Exception;
}
