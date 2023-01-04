from array import array
from inspect import isbuiltin
from os import remove
import random
from re import T
from util import genProgram

NUM = "NUM"
FALSE_exp = "FALSE"
VAR = "VAR"
PLUS = "PLUS"
TIMES = "TIMES"
LT = "LT"
AND = "AND"
NOT = "NOT"
ITE = "ITE"

ALLOPS = [NUM, FALSE_exp, VAR, PLUS, TIMES, LT, AND, NOT, ITE]

class Error(Exception):
    def __init__(self, message):
        self.message = message

    def __str__(self):
        return sefl.messagd



# *************************************************
# ***********  AST node definitions ***************
# *************************************************

class Node:
    def __str__(self):
        raise Error("Unimplemented method: str()")

    def interpret(self):
        raise Error("Unimplemented method: interpret()")

class FalseExp(Node):
    def __init__(self):
        self.type = FALSE_exp

    def __str__(self):
        return "false"

    def interpret(self, envt):
        return False

class Var(Node):
    def __init__(self, name):
        self.type = VAR
        self.name = name

    def __str__(self):
        return self.name

    def interpret(self, envt):
        return envt[self.name]


class Num(Node):
    def __init__(self, val):
        self.type = NUM
        self.val = val

    def __str__(self):
        return str(self.val)

    def interpret(self, envt):
        return self.val

class Plus(Node):
    def __init__(self, left, right):
        self.type = PLUS
        self.left = left
        self.right = right

    def __str__(self):
        return "(" + str(self.left) + "+" + str(self.right) +")"

    def interpret(self, envt):
        return self.left.interpret(envt) + self.right.interpret(envt)

class Times(Node):
    def __init__(self, left, right):
        self.type = TIMES
        self.left = left
        self.right = right

    def __str__(self):
        return "(" + str(self.left) + "*" + str(self.right) + ")"

    def interpret(self, envt):
        return self.left.interpret(envt) * self.right.interpret(envt)

class Lt(Node): #Less than 
    def __init__(self, left, right):
        self.type = LT
        self.left = left
        self.right = right

    def __str__(self):
        return "(" + str(self.left) + "<" + str(self.right) + ")"

    def interpret(self, envt):
        return self.left.interpret(envt) < self.right.interpret(envt)

class And(Node):
    def __init__(self, left, right):
        self.type = AND
        self.left = left
        self.right = right

    def __str__(self):
        return "(" + str(self.left) + "&&" + str(self.right) + ")"

    def interpret(self, envt):
        return self.left.interpret(envt) and self.right.interpret(envt)

class Not(Node):
    def __init__(self, left):
        self.type = NOT
        self.left = left

    def __str__(self):
        return "(!" + str(self.left)+ ")"

    def interpret(self, envt):
        return not self.left.interpret(envt)

class Ite(Node):    #If-then-else
    def __init__(self, c, t, f):
        self.type = ITE
        self.cond = c
        self.tcase = t
        self.fcase = f

    def __str__(self):
        return "(if " + str(self.cond) + " then " + str(self.tcase) + " else " + str(self.fcase) + ")"

    def interpret(self, envt):
        if (self.cond.interpret(envt)):
            return self.tcase.interpret(envt)
        else:
            return self.fcase.interpret(envt)

#Method checks if program synthesized is correct by checking input-output pairs
def isCorrect(program, inputoutputs):
    count = 0
    for i, inputoutput in enumerate(inputoutputs):
        out = program.interpret(inputoutput)
        #print (f'{i+1}. Evaluating programs {program}\non inputoutput examples {inputoutput}. The output of the program is {out}\n')
        if (out == inputoutput["_out"]):
            count += 1
        else:
            break
    return (len(inputoutputs) == count)

#####################################################################################

# *************************************************
# ****************  Problem 1a ********************
# *************************************************

def evaluate(expression, dict):
    return expression.interpret(dict)

def elimEquivalents(plist, inputoutputs):
    storedResultsInt = set()
    storedResultsBool = set()
    primeList = []
    for p in plist:
        tempInt = []
        tempBool = []
        for d in inputoutputs: 
            if isNumber(p):
                intValue = evaluate(p,d)
                tempInt += [intValue]
            elif isBoolean(p):
                boolValue = evaluate(p,d)
                tempBool += [boolValue]
        tempIntTuple = tuple(tempInt)
        tempBoolTuple = tuple(tempBool)
        if isNumber(p) and tempIntTuple not in storedResultsInt:
            storedResultsInt.add(tempIntTuple)
            primeList += [p]
        elif isBoolean(p) and tempBoolTuple not in storedResultsBool:
            storedResultsBool.add(tempBoolTuple)
            primeList +=[p]
    return primeList

def isNumber(p):
    if isinstance(p,Num) or isinstance(p,Var) or isinstance(p,Plus) or isinstance(p,Times) or isinstance(p,Ite):
        return True
    return False

def isBoolean(p):
    if isinstance(p,FalseExp) or isinstance(p,Lt) or isinstance(p,Not) or isinstance(p,And):
        return True
    return False

def grow(plist, intOps, boolOps):
    primeList = []
    boolList = []
    tlist = [t for t in plist if isNumber(t)]
    blist = [b for b in plist if isBoolean(b)]
    plus = False
    times = False
    if "PLUS" in intOps:
        plus = True
    if "TIMES" in intOps:
        times = True
    if "NOT" in boolOps:
        for b in blist:
            if Not(b) not in boolList:
                boolList += [Not(b)]
    if "AND" in boolOps:
        for bp in blist:
            for bz in blist:
                if And(bz,bp) not in boolList and bz!=bp:
                    boolList += [And(bp,bz)]
    for p in tlist:
        for z in tlist:
            if z!=p and "LT" in boolOps:
                boolList += [Lt(p,z)]
            if not isinstance(p,Ite) and not isinstance(z,Ite) and plus and Plus(z,p) not in primeList:
                primeList += [Plus(p,z)]
            if not isinstance(p,Ite) and not isinstance(z,Ite) and times and Times(z,p) not in primeList:
                primeList += [Times(p,z)] 
            if "ITE" in intOps:
                for b in blist:
                    if p!=z:
                        primeList += [Ite(b,p,z)]   
    primeList += boolList
    return primeList

#globalBnd: Maximum depth allowed for the AST (Number of layers to perform Bottom-Up Synthesis)
#intOps: A list of integer AST nodes the program synthesizer is allowed to use.
#boolOps: A list of boolean AST nodes the program synthesizer is allowed to use.
#vars: A list of all the variable names that can appear in the generated expressions.
#consts: A list of all the integer constants that can appear in the generated expressions.
#inputoutputs: A list of inputs/outputs to the function. Each element in the list is a map from variable names to values,
    #with the variable "_out" referring to the expected output of that given input.
#returns an AST/program that satisfies all input-output pairs. If none are found, the synthesizer will return "FAIL"
def bottomUp(globalBnd, intOps, boolOps, vars, consts, inputoutputs):
    plist = []  #plist = list of programs generated
    blist = []
    primeList = []
    #Convert initial input into AST nodes
    for v in vars: 
        plist += [Var(v)]
    for c in consts:
        plist += [Num(c)]
    for p in plist:
        for z in plist:
            if Lt(p,z) not in blist and Lt(z,p) not in blist and z!=p and "LT" in boolOps:
                blist += [Lt(p,z)]
            if Plus(p,z) not in primeList and Plus(z,p) not in primeList and "PLUS" in intOps:        
                primeList += [Plus(p,z)]
            if Times(p,z) not in primeList and Times(z,p) not in primeList and "TIMES" in intOps:        
                primeList += [Times(p,z)] 
    primeList += blist
    plist += primeList
    plist = elimEquivalents(plist,inputoutputs)
    for p in plist:
        if(isCorrect(p,inputoutputs)):
            return p
    globalBnd = globalBnd - 1
    while globalBnd > 0:
        plist += grow(plist,intOps,boolOps)
        plist = elimEquivalents(plist, inputoutputs)         
        for p in plist:
            if(isCorrect(p,inputoutputs)):
               return p
        globalBnd = globalBnd - 1
    return "FAIL"
    

def runTest():
    testa = bottomUp( #if x < y then x*5 else y*11
                    3,
                    [VAR, NUM, PLUS, TIMES, ITE],
                    [AND, NOT, LT, FALSE_exp],
                    ["x", "y"],
                    [5,11],
                    [{"x":10,"y":2, "_out":22},
                    {"x":11,"y":10,"_out":110},
                    {"x":2,"y":2,"_out":22},
                    {"x":5,"y":19,"_out":25}]
        )
    print("testa",testa)
    testb = bottomUp( #(x<6) && (x<y)       or !(y<x)
                    3,
                    [VAR, NUM, PLUS, TIMES, ITE],
                    [AND, NOT, LT, FALSE_exp],
                    ["x", "y"],
                    [6,11],
                    [{"x":10,"y":2, "_out":False},
                    {"x":11,"y":10,"_out":False},
                    {"x":2,"y":2,"_out":True},
                    {"x":5,"y":19,"_out":True},
                    {"x":3,"y":2,"_out":False}]
        )
    print("testb",testb)

    testc = bottomUp( # x+x+x
                    3,
                    [VAR, NUM, PLUS, TIMES, ITE],
                    [AND, NOT, LT, FALSE_exp],
                    ["x", "y"],
                    [],
                    [{"x":10,"y":2, "_out":30},
                    {"x":11,"y":10,"_out":33},
                    {"x":2,"y":2,"_out":6},
                    {"x":5,"y":19,"_out":15},
                    {"x":3,"y":2,"_out":9}]
        )
    print("testc",testc)

if __name__ == '__main__':
    runTest()