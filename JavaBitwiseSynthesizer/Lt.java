package proglang;

public class Lt extends Condition{
	
	Node first;
	Node second;
	
	public Lt(Node num1, Node num2) {
		this.first = num1;
		this.second = num2;
	}
	
	@Override
	public boolean evaluate(int num) {
		return this.first.evaluate(num) < this.second.evaluate(num);
	}
	
	@Override
	public String toString() {
		return "(" + first + "<" + second + ")";
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Lt)) {
			return false;
		}

		Lt c = (Lt) o;

		return this.first == c.first && this.second == c.second;
	}
}
