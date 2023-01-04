# ProgramSynthesizers
The following codes in Java and Python are program synthesizers that use a bottom-up approach.

Given a input-output list, the synthesizers will try to generate a program such that it matches the desired input-output list. 

In Python, the synthesizer is more broad and can be used to generate boolean and arithmetic expressions to satisfy the input-output list.

For instance, given the variables x and y, the constants 5 and 11, as well as the following input-output pairs below:
>{"x":10,"y":2, "_out":22}
{"x":11,"y":10,"_out":110}
{"x":2,"y":2,"_out":22}
{"x":5,"y":19,"_out":25}

The synthesizer will generate the following program: (if x < y then x * 5 else y * 11) as it satisfies the input-output pairs given.  

Another example,  given the variables x and y, the constant 0, as well as the following input-output pairs below:
>{"x":2, "y":55, "_out":False}
{"x":32, "y":3, "_out":False}
{"x":-5, "y":-3, "_out":True}
{"x":-7, "y":-10, "_out":True}
{"x":-20, "y":8, "_out":False}
{"x":20, "y":-8, "_out":False}

The synthesizer will generate the following program: ((x<0) and (y<0)) as it satisfies the input-output pairs given.  

In Java, I modified the synthesizer into a bitwise synthesizer. This means the generator will only synthesize arithemetic programs using bitwise expressions. A list of examples can be found in the Java Driver file.

In both cases, the synthesizers are heavily restricted with depth. As bottom-up approaches are computationtally expensive and exhaustive, further refinements should be done to make the synthesizers practical for segments of code that can be generated with a greater depth than 3. Some examples of refinement or alternatives are listed below.

>Bottom-up Refinement
-When synthesizing programs for new depths, it is possible to avoid generating programs that do not provide satisfactory new programs. An example of this is the ~ operation represented by the Not node in the Bitwise Synthesizer. At depth one, ~f(x) can be a program we want to check but ~(~f(x)) at the next level can be a redundant and non-useful program we wish to remove or prune away as it is the same program at a previous depth.

>STUN
-A common use in both synthesizers is deciding when to make decisions following the format if(condition) then trueCase else falseCase. These Ite expressions can be computationally expensive to enumerate and test individually so alternative more efficient techniques such as STUN can be implemented to efficiently synthesize top-level ITE expressions. (Ref: https://people.csail.mit.edu/asolar/SynthesisCourse/Lecture3.htm)


