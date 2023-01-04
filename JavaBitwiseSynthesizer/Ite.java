package proglang;

public class Ite extends Node{

	Condition condition;
	Node trueValue;
	Node falseValue;
	
	public Ite(Condition cond, Node trueValue, Node falseValue) {
		this.condition = cond;
		this.trueValue = trueValue;
		this.falseValue = falseValue;
	}
	
	@Override
	public int evaluate(int num) {
		if(condition.evaluate(num))
			return trueValue.evaluate(num);
		return falseValue.evaluate(num);
	}
	
	@Override
	public String toString() {
		return "if " + condition + " \n\tthen " + trueValue + " \n\telse " + falseValue;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Ite)) {
			return false;
		}

		Ite c = (Ite) o;

		return this.condition == c.condition && this.trueValue == c.trueValue && this.falseValue == c.falseValue;
	}
}
