package proglang;

public class Xor extends Node{
	
	Node first;
	Node second;
	
	public Xor(Node num1, Node num2) {
		this.first = num1;
		this.second = num2;
	}
	
	@Override
	int evaluate(int num) {
		return first.evaluate(num) ^ second.evaluate(num);
	}
	
	@Override
	public String toString() {
		return "(" + first + "^" + second +")";
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Xor)) {
			return false;
		}

		Xor c = (Xor) o;

		return this.first == c.first && this.second == c.second;
	}
}
