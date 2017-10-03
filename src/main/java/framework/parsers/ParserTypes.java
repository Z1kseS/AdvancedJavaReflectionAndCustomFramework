package framework.parsers;

public enum ParserTypes {
	PROPERTY("property"), CONSTRUCTOR("constructor-arg"), BEAN("bean"), INTERCEPTOR("interceptor");

	private String propertyName;

	ParserTypes(String propertyName) {
		this.propertyName = propertyName;
	}

	public String propertyName() {
		return propertyName;
	}
}
