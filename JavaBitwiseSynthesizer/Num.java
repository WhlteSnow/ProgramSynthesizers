package proglang;

public class Num extends Node{

	int value;
	
	public Num(int num) {
		this.value = num;
	}
	
	@Override
	public int evaluate(int num) {
		return this.value;
	}

	@Override
	public String toString() {
		return "" + this.value;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Num)) {
			return false;
		}

		Num c = (Num) o;

		return this.value == c.value;
	}
}
