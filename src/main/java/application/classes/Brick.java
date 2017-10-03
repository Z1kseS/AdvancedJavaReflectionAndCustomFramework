package application.classes;

public class Brick {
	private Brick brick;
	private String name;

	public Brick(Brick brick, String name) {
		this.brick = brick;
		this.name = name;
	}

	public Brick(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Brick [memory=" + System.identityHashCode(this) + " brick=" + brick + ", name=" + name + "]";
	}
}
