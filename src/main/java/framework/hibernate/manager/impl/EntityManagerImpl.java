package framework.hibernate.manager.impl;

import java.io.FileInputStream;
import java.io.IOException;
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
import framework.hibernate.manager.EntityManager;
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

	public void loadProperties(String fileName) throws IOException, ClassNotFoundException {
		Properties props = new Properties();
		FileInputStream fis = null;
		fis = new FileInputStream(fileName);
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
			result.add(resultSetParser.getObject(entityClass, resultSet));
		}
		return result;
	}

}
