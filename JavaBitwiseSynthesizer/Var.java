package proglang;

public class Var extends Node {

	int value;
	
	public Var(int value) {
		this.value = value;
	}

	@Override
	public int evaluate(int num) {
		return num;
	}

	@Override
	public String toString() {
		return "x";
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Var)) {
			return false;
		}

		Var c = (Var) o;

		return this.value == c.value;
	}
}
