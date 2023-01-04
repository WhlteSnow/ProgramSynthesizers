package proglang;

public class Shl extends Node{

	Node value;
	Node shift;
	
	public Shl(Node num, Node shift) {
		this.value = num;
		this.shift = shift;
	}	
	
	@Override
	public int evaluate(int num) {
		return this.value.evaluate(num) << this.shift.evaluate(num);
	}
	
	@Override
	public String toString() {
		return "(" + value +"<<" + shift + ")";
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Shl)) {
			return false;
		}

		Shl c = (Shl) o;

		return this.value == c.value && this.shift == c.shift;
	}
}
