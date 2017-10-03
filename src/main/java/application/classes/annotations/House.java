package application.classes.annotations;

import framework.core.annotations.Autowiring;
import framework.core.annotations.Component;

@Component("house")
public class House {

	@Autowiring
	private Kitchen kitchen;

	public Kitchen getKitchen() {
		return kitchen;
	}

	public void setKitchen(Kitchen kitchen) {
		this.kitchen = kitchen;
	}

	@Override
	public String toString() {
		return "House [kitchen=" + kitchen + "]";
	}
}
