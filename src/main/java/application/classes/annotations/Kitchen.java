package application.classes.annotations;

import framework.core.annotations.Autowiring;
import framework.core.annotations.Component;

@Component("kitchen")
public class Kitchen {

	@Autowiring
	private Table table;

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	@Override
	public String toString() {
		return "Kitchen [table=" + table + "]";
	}

}
