package framework.hibernate.manager;

import java.io.IOException;
import java.util.List;

public interface EntityManager {
	<T> List<T> getEntities(Class<T> entityClass) throws Exception;

	void loadProperties(String fileName) throws IOException, ClassNotFoundException;
}
