package proglang;

public class And extends Node{

	Node first;
	Node second;
	
	public And(Node num1, Node num2) {
		this.first = num1;
		this.second = num2;
	}
	
	@Override
	int evaluate(int num) {
		return first.evaluate(num) & second.evaluate(num);
	}

	@Override
	public String toString() {
		return "(" + first + "&" + second +")";
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof And)) {
			return false;
		}

		And c = (And) o;

		return this.first == c.first && this.second == c.second;
	}
}
