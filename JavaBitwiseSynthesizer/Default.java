package proglang;

public class Default extends Condition{

	boolean value;
	
	public Default(boolean value) {
		this.value = value;
	}
	
	@Override
	public boolean evaluate(int num) {
		return value;
	}
	
	@Override
	public String toString() {
		if(value)
			return "(true)";
		return "(false)";
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Default)) {
			return false;
		}

		Default c = (Default) o;
		return this.value == c.value;
	}
}
