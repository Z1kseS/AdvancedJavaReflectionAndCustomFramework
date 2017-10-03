package framework.parsers;

public class Property {
	private String name;
	private Bean bean;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Bean getBean() {
		return bean;
	}

	public void setBean(Bean bean) {
		this.bean = bean;
	}

	@Override
	public String toString() {
		return "Property [name=" + name + ", bean=" + bean + "]";
	}
}
