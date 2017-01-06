package a3posted;

//  COMP 250 - Introduction to Computer Science - Fall 2016
//  Assignment #3 - Question 1

//  Miral Toufaili 260573212


import java.util.*;

/*
 *  Trie class.  Each node is associated with a prefix of some key 
 *  stored in the trie.   (Any string is a prefix of itself.)
 */

public class Trie
{
	private TrieNode root;

	// Empty trie has just a root node.  All the children are null.

	public Trie() 
	{
		root = new TrieNode();
	}

	public TrieNode getRoot(){
		return root;
	}

	
	/*
	 * Insert key into the trie.  First, find the longest 
	 * prefix of a key that is already in the trie (use getPrefixNode() below). 
	 * Then, add TrieNode(s) such that the key is inserted 
	 * according to the specification in PDF.
	 */
	public void insert(String key)
	{
		//  ADD YOUR CODE BELOW HERE

	      TrieNode prefixNode = getPrefixNode(key);
	      
	        // Base case: If key already exists in the tree
	        if (prefixNode.getDepth() == key.length()) {
	        	prefixNode.setEndOfKey(true);
	        }  
	        
	      
	      // adding the nodes from the first letter that differs to the end of the string
	      for ( int index = prefixNode.getDepth(); index < key.length(); index++){ 	  
	    	  prefixNode = prefixNode.createChild(key.charAt(index));
	      }
	      prefixNode.setEndOfKey(true); 
	
	      

		//  ADD YOUR ABOVE HERE
	}
	
	
	

	// insert each key in the list (keys)

	public void loadKeys(ArrayList<String> keys)
	{
		for (int i = 0; i < keys.size(); i++)
		{
			insert(keys.get(i));
		}
		return;
	}

	/*
	 * Given an input key, return the TrieNode corresponding the longest prefix that is found. 
	 * If no prefix is found, return the root. 
	 * In the example in the PDF, running getPrefixNode("any") should return the
	 * dashed node under "n", since "an" is the longest prefix of "any" in the trie. 
	 */
	private TrieNode getPrefixNode(String key)
	{
		
		//   ADD YOUR CODE BELOW HERE
		
        
        // For each character, get next child until null or end of string.
        // A null return means that the word was either longer than any existing keys, or contained an order of characters previously not in the tree.
		
		
		
		TrieNode current = this.root;
		   int i = 0;
		   
		   while ( i < key.length()){  //goes through the key char by char  
			   
			   // checking if this is the longest prefix.
			   //once we hit null, we return the last node
			   
			   if (current.getChild(key.charAt(i)) == null)
				   return current;    
			   
			   // exists ==> we get the child
			   else
				   current = current.getChild(key.charAt(i));
			   i++;
		   }
		   return current;   // ==> key is in the tree

		   
		//   return null  //  REPLACE THIS STUB
	
		//   ADD YOUR CODE ABOVE HERE

	}

	/*
	 * Similar to getPrefixNode() but now return the prefix as a String, rather than as a TrieNode.
	 */

	public String getPrefix(String key)
	{
		return getPrefixNode(key).toString();
	}

	
	/*
	 *  Return true if key is contained in the trie (i.e. it was added by insert), false otherwise.
	 *  Hint:  any string is a prefix of itself, so you can use getPrefixNode().
	 */
	public boolean contains(String key)
	{  
		//   ADD YOUR CODE BELOW HERE
		
		 TrieNode prefixNode = getPrefixNode(key);
		 boolean length = prefixNode.getDepth() == key.length(); // depth was set private, I needed it here so I created a method to get 
		
		 return (length && prefixNode != null  && prefixNode.isEndOfKey());
		
		//   ADD YOUR CODE ABOVE HERE
	}

	/*
	 *  Return a list of all keys in the trie that have the given prefix. 
	 */
	public ArrayList<String> getAllPrefixMatches( String prefix )
	{
		//  ADD YOUR CODE BELOW 
		
		  TrieNode prefixNode = getPrefixNode(prefix);
		  ArrayList<String> stringList = new ArrayList<String>();
		   
		  
		  if(prefixNode == root){ //base case: checks for prefix
			  return stringList; 
		  } 
		  
		  
		  if(prefixNode.isEndOfKey()){  //check if it's a word
			  stringList.add(prefixNode.toString());
		  }
		
		  
		  for(int i = 0; i < 256; i++){
			  
			  TrieNode current = prefixNode.getChild((char) i);
			  
			  if(current != null){
				  
				  ArrayList<String> currentList =  getAllPrefixMatches(current.toString());
				  
				   for(int j = 0; j < currentList.size(); j++){
					   
				   	   stringList.add(currentList.get(j));  
				    }
			   }
		  }
		
		//  ADD YOUR CODE ABOVE HERE

		return stringList;
	}

	
	/*
	 *  A node in a Trie (prefix) tree.  
	 *  It contains an array of children: one for each possible character.
	 *  The ith child of a node corresponds to character (char)i
	 *  which is the UNICODE (and ASCII) value of i. 
	 *  Similarly the index of character c is (int)c.
	 *  So children[97] = children[ (int) 'a']  would contain the child for 'a' 
	 *  since (char)97 == 'a'   and  (int)'a' == 97.
	 * 
	 * References:
	 * -For all unicode charactors, see http://unicode.org/charts
	 *  in particular, the ascii characters are listed at http://unicode.org/charts/PDF/U0000.pdf
	 * -For ascii table, see http://www.asciitable.com/
	 * -For basic idea of converting (casting) from one type to another, see 
	 *  any intro to Java book (index "primitive type conversions"), or google
	 *  that phrase.   We will cover casting of reference types when get to the
	 *  Object Oriented Design part of this course.
	 */

	private class TrieNode
	{
		/*  
		 *   Highest allowable character index is NUMCHILDREN-1
		 *   (assuming one-byte ASCII i.e. "extended ASCII")
		 *   
		 *   NUMCHILDREN is constant (static and final)
		 *   To access it, write "TrieNode.NUMCHILDREN"
		 */

		public static final int NUMCHILDREN = 256;

		private TrieNode   parent;
		private TrieNode[] children;
		private int        depth;            // 0 for root, 1 for root's children, 2 for their children, etc..
		private char       charInParent;    // Character associated with edge between this node and its parent.
		        			        		// See comment above for relationship between an index in 0 to 255 and a char value.
		private boolean endOfKey;   // Set to true if prefix associated with this node is also a key.

		// Constructor for new, empty node with NUMCHILDREN children.  All the children are null. 

		public TrieNode()
		{
			children = new TrieNode[NUMCHILDREN];
			endOfKey = false;
			depth = 0; 
			charInParent = (char)0; 
		}

		
		/*
		 *  Add a child to current node.  The child is associated with the character specified by
		 *  the method parameter.  Make sure you set all fields in the child node.
		 *  
		 *  To implement this method, see the comment above the inner class TrieNode declaration.  
		 */
		public TrieNode createChild(char  c) 
		{	   

			
	        TrieNode child = new TrieNode();
	        
			// ADD YOUR CODE BELOW HERE
			
	        
	        //links to parent
	        child.parent = this; 
	        child.depth = this.depth + 1; 
	       
	        // stores character
	        children[(int) c] = child; 
	        child.charInParent = c; 
	        
	        
	        return child;
	    }

			

			// ADD YOUR CODE ABOVE HERE


		// Get the child node associated with a given character, i.e. that character is "on" 
		// the edge from this node to the child.  The child could be null.  

		public TrieNode getChild(char c) 
		{
			return children[ c ];
		}
		
		// I need the depth and it's set to private. had to create a get method
		 public int getDepth() 
		   {
		      return depth;
		   }
		

		// Test whether the path from the root to this node is a key in the trie.  
		// Return true if it is, false if it is prefix but not a key.

		public boolean isEndOfKey() 
		{
			return endOfKey;
		}

		// Set to true for the node associated with the last character of an input word

		public void setEndOfKey(boolean endOfKey)
		{
			this.endOfKey = endOfKey;
		}

		/*  
		 *  Return the prefix (as a String) associated with this node.  This prefix
         *  is defined by descending from the root to this node.  However, you will
         *  find it is easier to implement by ascending from the node to the root,
         *  composing the prefix string from its last character to its first.  
		 *
		 *  This overrides the default toString() method.
		 */

		public String toString()
		{
			// ADD YOUR CODE BELOW HERE
			
			 if (parent != null)
				 
			    	return parent.toString() + charInParent;
			 
			    else
			    	
			    	return "";
			    

			// ADD YOUR CODE ABOVE HERE
		}
	}
}


