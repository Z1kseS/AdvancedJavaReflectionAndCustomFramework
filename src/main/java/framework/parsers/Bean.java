package framework.parsers;

import java.util.ArrayList;
import java.util.List;

public class Bean {

	private String name;
	private String className = "java.lang.String";

	public enum Scope {
		Prototype("prototype"), Singleton("singleton");

		private String name;

		private Scope(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	};

	private List<Bean> constructorArg = new ArrayList<>();
	private List<Property> properties = new ArrayList<>();

	private String value;

	private boolean isPrototype;

	private boolean isStub;

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	public List<Bean> getConstructorArg() {
		return constructorArg;
	}

	public void setConstructorArg(List<Bean> constructorArg) {
		this.constructorArg = constructorArg;
	}

	@Override
	public String toString() {
		return "Bean [name=" + name + ", className=" + className + ", constructorArg=" + constructorArg
				+ ", properties=" + properties + ", value=" + value + ", isStub=" + isStub + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		if (className != null)
			this.className = className;
	}

	public boolean isStub() {
		return isStub;
	}

	public void setStub(boolean isStub) {
		this.isStub = isStub;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bean other = (Bean) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public boolean isPrototype() {
		return isPrototype;
	}

	public void setPrototype(String scope) {
		if (scope == null)
			return;
		else if (scope.equals(Scope.Prototype.getName()))
			this.isPrototype = true;
	}

}
