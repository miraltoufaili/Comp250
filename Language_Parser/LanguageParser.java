//package a2posted;

// Name: Miral Toufaili



import java.util.LinkedList;
/**
 * This static class provides the isStatement() method to parse a sequence 
 * of tokens and to decide if it forms a valid statement
 * You are provided with the helper methods isBoolean() and isAssignment().
 * 
 * - You may add other methods as you deem necessary.
 * - You may NOT add any class fields.
 */
public class LanguageParser {
	
	/**
	 * Returns true if the given token is a boolean value, i.e.
	 * if the token is "true" or "false".
	 * 
	 * DO NOT MODIFY THIS METHOD.
	 */
	
	private static boolean isBoolean (String token) {
		
		return (token.equals("true") || token.equals("false"));
		
	}
	
	/**
	 * Returns true if the given token is an assignment statement of the
	 * type "variable=value", where the value is a non-negative integer.
	 * 
	 * DO NOT MODIFY THIS METHOD.
	 */
	private static boolean isAssignment (String token) {
		
		// The code below uses Java regular expressions. You are NOT required to
		// understand Java regular expressions, but if you are curious, see:
		// <http://java.sun.com/javase/6/docs/api/java/util/regex/Pattern.html>
		//
		//   This method returns true iff 
		//   the token matches the following structure:
		//   one or more letters (a variable), followed by
		//   an equal sign '=', followed by
		//   one or more digits.
		//   However, the variable cannot be a keyword ("if", "then", "else", 
		//   "true", "false")
		
		if (token.matches("if=\\d+") || token.matches("then=\\d+") ||
				token.matches("else=\\d+") || token.matches("end=\\d+") ||
				token.matches("true=\\d+") || token.matches("false=\\d+"))
			return false;
		else
			return token.matches("[a-zA-Z]+=\\d+");
		
	}

	/**
	 * Given a sequence of tokens through a StringSplitter object,
	 * this method returns true if the tokens can be parsed according
	 * to the rules of the language as specified in the assignment.
     */
				
	public static boolean  isStatement(StringSplitter splitter) {

		StringStack stack = new StringStack();
		int count = splitter.countTokens();
		String token;
	//   ADD YOUR CODE HERE
	
		//There are two possibilities for a right answer: either a statment thus the count ==  one
		// or a if-then-else-end where the count should be at least 7, since if will be already outside our count
		// then we need at least 7 therefore count should be > 6 
		// any number inbetween 1 and 7 is not possible
		
		
	if (count == 0)
			return false;
	
	// this tests if the input is a single statement 
    else if (count == 1)  {  
		token = splitter.nextToken();
			   
			if( isAssignment(token)){	
				return true;
			}else { return false; 
			      }
			
	 
					
   } else if (count > 6) { // if we have less than 7 then we can't have the right format for if-then-else-end
	  
	   token = splitter.nextToken();
	  
				do {
					
					// Test for if: if it's an if, we PUSH it to our stack
					
				 if (token.equals("if") && splitter.countTokens() > 5){ // this will carry the test when the input starts with if
						stack.push(token); 
						token = splitter.nextToken();  
					  } else if(token.equals("if") && splitter.countTokens() < 5){ // if it starts with if but the count is less than it should ===> it's false
						  return false; 
					  }
					
					
				 
				 	// Test for bool:  PUSH it to our stack if we peek and we find an IF
				  if(isBoolean(token)){ //
						 if (stack.peek().equals("if")){
							 stack.push("bool"); 
							 token = splitter.nextToken(); 
					     }else if(!stack.peek().equals("if")){
					    	 return false;
					     }
				  } 
					 
					 //Test for then: if the stack is empty we return a falst
				  	 //				  if we peek and we find a bool (that only exists if we have an if)
				     //			==> we will POP the if and bool and PUSH then.
				  	 //	 this means that whenever we have a then, it means that we had an if-bool
					
				  if (token.equals("then")){
				    	if (stack.isEmpty()) {
				    		return false;
						} else if (stack.peek().equals("bool")){
							stack.pop();
						    stack.pop();
						    stack.push(token); 
							token = splitter.nextToken(); 
						}
				    } 
				  
				    if(token.equals("if")){ // if our next token is an if, then we exit this loop (rather than go through the tests of else/end
				    	continue; 
				    }
				    

				    //Test for Statement: if we peek and find a then
				    // we push "statement"
				    // if we peek and don't find a then or an else == > it's false
			
				 if (isAssignment(token)) {
					 if(stack.peek().equals("then") ){
						 stack.push("statement"); 
						 token = splitter.nextToken();  
				 } else if (!stack.peek().equals("then") || !stack.peek().equals("else")){ 
					 return false; 
				   }	
				 }
				 
				
				 //Test for Else: we peek: if we find a statement or end then we PUSH else
				 //if we had a statement in the form of (if-then-else-end) we remove it
				 // and we add to the stack ADDED so we know that THERE WAS A VALID STATEMENT at that position
				 // if we peek and find ADDED then we also push it, it means that before this else, a statement has happened
				 
				 if (token.equals("else")){
					 if( stack.peek().equals("statement")){
						      stack.push(token);
						      token = splitter.nextToken();
					 } else if( stack.peek().equals("ADDER")){
							 stack.push(token);
							 token = splitter.nextToken(); 
					 } else { return false; 
				     }
				 }	
				
				 if (token.equals("if")){
					continue;
				 }
					
					
				//Test for Statement AFTER ELSE: we peek, if there's an else we PUSH STATMENT
				
				if (isAssignment(token)){
					if(stack.peek().equals("else")){
						stack.push("statement"); 
						token = splitter.nextToken(); 
				    }else if (!stack.peek().equals("then") || !stack.peek().equals("else")){ 
				    	return false; 
				    }
				 }	
				
				 
				//Test for end: At this point, then is representing IF_BOOL
				//Statement-else-statement are PUSHED
				//if the stack is empty, then it's false
				// if not, then we pop: STATEMENT-ELSE-STATMELT-THEN
				// we peek, if we have a then or an else, then what we POPed was a statment 
				// therefore we PUSH our ADDED that represents that a valid statemnet of the if-then-else-end was there
				
				
				if (token.equals("end")){
					if (stack.isEmpty()) {
						return false;
					} else {
							stack.pop();
						    stack.pop();
						    stack.pop();
						    stack.pop();
						   }
				    if (stack.peek().equals("then") || stack.peek().equals("else")){
				    	stack.push("ADDER");
				    }
				    token = splitter.nextToken(); 
					} 

				} while(splitter.hasMoreTokens() || !token.equals("") );
				
				// our loop keeps going as long as there is a nexttoken or the token is not null (what if after end we just have an else? we wont have a nexttoken but we have a token not counted for!)
				
				// after going through the tokens, if the stack is empty, which means everything was pushed and poped ==> things match ==. it's a true statment
			
				if (stack.isEmpty()){
				return true; 
			}		
       } 
		 return false; 
	}
}
			
		
	
	
	



