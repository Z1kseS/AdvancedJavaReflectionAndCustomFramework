package application.hw3;

import framework.core.annotations.Component;
import framework.core.annotations.Statefull;

@Component("statefullCounter")
@Statefull
public class StatefullCounter {
	private int number = 0;

	public int getNumber() {
		return number;
	}

	public void increment() {
		this.number++;
	}
}
