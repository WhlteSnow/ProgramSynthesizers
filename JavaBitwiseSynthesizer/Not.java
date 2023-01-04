package proglang;
	
public class Not extends Node{
	
	Node value;
	
	public Not(Node num) {
		this.value = num;
	}
	
	@Override
	public int evaluate(int num) {
		return ~value.evaluate(num);
	}
	
	@Override
	public String toString() {
		return "(~" + value +")";
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Not)) {
			return false;
		}

		Not c = (Not) o;

		return this.value == c.value;
	}
}
