package proglang;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class Driver {
	
	/**
	 * Method checks whether the given program satisfies all input-output examples
	 * @param program - program to test. 
	 * @param inputs - input list. 
	 * @param outputs - output list
	 * @return true if program satisfies all input-output examples, false otherwise
	 * */
	public static boolean correctProgram(Node program, int inputs[], int outputs[]) { 
		int success = 0;
		for(int j = 0; j < inputs.length; j++) {
			//System.out.println("Evaluating program: " + program + " Input = " + inputs[j] + " Result = " + program.evaluate(inputs[j]));
			if(program.evaluate(inputs[j]) != outputs[j]) {
				break;
			}
			success++;
		}
		if(success == inputs.length) {
			return true;
		}
		return false;
	}
	
	/**
	 * Method expands the given programList via Bottom-Up Synthesis
	 * @param programList - current list of programs 
	 * @param conditionList - current list of conditions
	 * */
	public static void expand(HashSet<Node> programList, HashSet<Condition> conditionList){
		HashSet<Node> addToProgramList = new HashSet<Node>();
		HashSet<Condition> addToConditionList = new HashSet<Condition>();
		
		for (Iterator<Node> i = programList.iterator(); i.hasNext();) {
		    Node element = i.next();
		    for (Iterator<Node> j = programList.iterator(); j.hasNext();) {
			    Node element2 = j.next();
			    if(element!=element2) {
			    	addToProgramList.add(new And(element,element2));	// & operation
			    	addToProgramList.add(new Or(element,element2));		// | operation
			    	addToProgramList.add(new Xor(element,element2));	// Xor operation
			    	addToProgramList.add(new Shl(element,element2));	// << operation
			    	addToProgramList.add(new Shr(element,element2));	// >> operation
			    	addToConditionList.add(new Lt(element, element2));  // Less than boolean operation
			    	for(Iterator<Condition> k = conditionList.iterator(); k.hasNext();) {
			    		Condition c = k.next();
			    		addToProgramList.add(new Ite(c,element,element2));	// Ite operation
			    	}
			    }
			}
		    addToProgramList.add(new Not(element));	// ~ operation
		}
		
		programList.addAll(addToProgramList);
		conditionList.addAll(addToConditionList);
	}
	
	
	/**
	 * Method reduces the given programList by eliminating similar programs. IE: If programs have same outputs.
	 * @param programList - current list of programs
	 * @param conditionList - current list of conditions
	 * @param input - list of inputs
	 * @param output - list of outputs
	 * */
	public static void reduce(HashSet<Node> programList, HashSet<Condition> conditionList, int input[], int output[]){
		HashSet<ArrayList<Integer>> programValues = new HashSet<ArrayList<Integer>>();
		HashSet<ArrayList<Boolean>> booleanValues = new HashSet<ArrayList<Boolean>>();
		HashSet<Node> programToRemove = new HashSet<Node>();
		HashSet<Condition> conditionToRemove = new HashSet<Condition>();
		for (Iterator<Node> i = programList.iterator(); i.hasNext();) {
		    Node element = i.next();
		    ArrayList<Integer> tempValues = new ArrayList<Integer>();
		    for(int k =0; k <input.length; k++) {
		    	tempValues.add(element.evaluate(input[k]));	
		    }
		    if(!programValues.contains(tempValues)) {
		    	programValues.add(tempValues);
		    }
		    else {
		    	programToRemove.add(element);
		    }
		}
		programList.removeAll(programToRemove);
		
		for (Iterator<Condition> i = conditionList.iterator(); i.hasNext();) {
			Condition element = i.next();
			ArrayList<Boolean> tempConditions = new ArrayList<Boolean>();
			for(int k =0; k <input.length; k++) {
				tempConditions.add(element.evaluate(input[k]));	
		    }
			if(!booleanValues.contains(tempConditions)) {
				booleanValues.add(tempConditions);
		    }
		    else {
		    	conditionToRemove.add(element);
		    }
		}
		conditionList.removeAll(conditionToRemove);
	}
	
	/**
	 * Method takes the given input list, output list and constant list and returns a program.
	 * The program transforms the ith input in the input list into the ith output of the output list
	 * @param input - array of inputs
	 * @param output - array of outputs
	 * @param constants - array of constants
	 * @param dephth - number of layers the program will synthesize to
	 * @return program that transforms inputs into outputs
	 * */
	public static Node sampleProgram(int input[], int output[], int constants[], int depth) {
		int count = 1;
		HashSet<Node> programList = new HashSet<Node>();
		HashSet<Condition> conditionList = new HashSet<Condition>();
		HashSet<Node> addToProgramList = new HashSet<Node>();
		HashSet<Condition> addToConditionList = new HashSet<Condition>();
		Node inputVariable = new Var(1);	//substitute input as a variable
		Node inputVariable2 = new Var(2);	//substitute input as a variable
		programList.add(inputVariable);
		programList.add(inputVariable2);
		for(int i =0; i< constants.length;i++) {//convert constant inputs into Node inputs
			Node temp = new Num(constants[i]);
			programList.add(temp);
		}
		Condition t = new Default(true);
		Condition f = new Default(false);
		conditionList.add(t);
		conditionList.add(f);
		for (Iterator<Node> i = programList.iterator(); i.hasNext();) {
		    Node element = i.next();
		    for (Iterator<Node> j = programList.iterator(); j.hasNext();) {
			    Node element2 = j.next();
			    if(element!=element2) {
			    	addToProgramList.add(new And(element,element2));	// & operation
			    	addToProgramList.add(new Or(element,element2));		// | operation
			    	addToProgramList.add(new Xor(element,element2));	// Xor operation
			    	addToProgramList.add(new Shl(element,element2));	// << operation
			    	addToProgramList.add(new Shr(element,element2));	// >> operation
			    	addToConditionList.add(new Lt(element, element2));  // Less than boolean operation
			    	for(Iterator<Condition> k = conditionList.iterator(); k.hasNext();) {
			    		Condition c = k.next();
			    		addToProgramList.add(new Ite(c,element,element2));	// Ite operation
			    	}
			    }
			}
		    addToProgramList.add(new Not(element));	// ~ operation
		}
		
		programList.addAll(addToProgramList);
		conditionList.addAll(addToConditionList);
		
		reduce(programList, conditionList, input, output);
		
		for (Iterator<Node> i = programList.iterator(); i.hasNext();) {
			Node element = i.next();
			if(correctProgram(element, input, output)) {
				return element;
			}
		}
		while(count < depth) {
			expand(programList,conditionList);
			reduce(programList, conditionList, input, output);
			for (Iterator<Node> i = programList.iterator(); i.hasNext();) {
				Node element = i.next();
				if(correctProgram(element, input, output)) {
					return element;
				}
			}
			count++;
		}
		
		return null;
	}
	
	public static void main(String[] args) {
		int input[] =  {3,4,5}; 
		int output[] = {-4,-5,-6};
		int constants[] = {0,1};
//		System.out.println(sampleProgram(input,output,constants,3));	//	~x
		
		int input2[] =  {2,3,5,6};
		int output2[] = {-3,-4,4,4};
		int constants2[] = {4};
//		System.out.println(sampleProgram(input2,output2,constants2,3));	//if(x<4) then ~x else x&4 

		int input3[] =  {2,3,4,5,6,7,8,9,10};
		int output3[] = {2,1,4,1,8,1,16,1,32};
		int constants3[] = {1};
//		System.out.println(sampleProgram(input3,output3,constants3,3));	//Generate a program such that odd numbers >1 return 1 and even numbers return 2^(input/2)
		
		int input4[] =  {3,4,5,6,8,9};
		int output4[] = {-2,-5,-6,-5,-9,-10};
		int constants4[] = {2,3};
//		System.out.println(sampleProgram(input4,output4,constants4,3));	//(x&2)|(~x)
		
		int input5[] =  {-1,-2,3,4,5};
		int output5[] = {-32,-64,96,128,160};
		int constants5[] = {5};
//		System.out.println(sampleProgram(input5,output5,constants5,3));	//x << 5
		
		int input6[] =  {2,3,4,5,6,7};
		int output6[] = {1,3,1,5,4,5};
		int constants6[] = {5};
//		System.out.println(sampleProgram(input6,output6,constants6,3));	//if ( 5 < 5^x) then x>>(5>>x) else if(x<5) then 5^x else x&5
		
		int input7[] =  {10,-2,15,-3,5,-7};
		int output7[] = {-21,3,-29,7,-9,15};
		int constants7[] = {1};
//		System.out.println(sampleProgram(input7,output7,constants7,3));	//(1<<x)>>(x^1)|(~(x<<1))
		
		int input8[] =  {10,-2,15,-3};
		int output8[] = {-12,0,-15,3};
		int constants8[] = {1};
//		System.out.println(sampleProgram(input8,output8,constants8,3));	//~(x^1)
		
		int input9[] =  {5,-2,6};
		int output9[] = {-20,0,-64};
		int constants9[] = {1,2};
//		System.out.println(sampleProgram(input9,output9,constants9,3));	//((x>>2)^(~x))<<(x>>1)
		
		int input10[] =  {1,2,3,4,5};
		int output10[] = {2,2,12,16,20};
		int constants10[] = {2};
//		System.out.println(sampleProgram(input10,output10,constants10,3));	//if(2 <x) then x <<2 else 2
		
	}
}
