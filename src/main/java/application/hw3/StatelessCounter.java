package application.hw3;

import framework.core.annotations.Component;
import framework.core.annotations.Stateless;

@Component("statelessCounter")
@Stateless
public class StatelessCounter {
	private int number = 0;

	public int getNumber() {
		return number;
	}

	public void increment() {
		this.number++;
	}
}
