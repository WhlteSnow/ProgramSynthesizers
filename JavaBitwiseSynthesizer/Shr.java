package proglang;

public class Shr extends Node{
	
	Node value;
	Node shift;
	
	public Shr(Node num, Node shift) {
		this.value = num;
		this.shift = shift;
	}	
	
	@Override
	public int evaluate(int num) {
		return value.evaluate(num) >> shift.evaluate(num);
	}
	
	@Override
	public String toString() {
		return "(" + value +">>" + shift + ")";
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Shr)) {
			return false;
		}

		Shr c = (Shr) o;

		return this.value == c.value && this.shift == c.shift;
	}
}
