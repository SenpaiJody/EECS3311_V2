package database;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TrieTreeNameIDSearch {

	//ALGORITHM ADAPTED FROM: https://www.geeksforgeeks.org/dsa/trie-insert-and-search/
	//this is used to "search" for the best matching ingredient when given a search term.
	
	//(i had experimented with the Levenshtein Distance, but I think this is more appropriate) -Jody
	
	TrieNode root;
	
	public TrieTreeNameIDSearch() {
		root = new TrieNode('.');
		
	}
	
	public void insert(String key, int id) {
        // Initialize the curr pointer with the root node
        TrieNode curr = root;

        // Iterate across the length of the string
        for (char c : key.toCharArray()) {

            // Check if the node exists for the
            // current character in the Trie
        	TrieNode next = null;
        	for (TrieNode child : curr.children) {
        		if (child.character==c) {
        			next = child;
        			break;
        		}
        	}
        	if (next == null) {
        		next = new TrieNode(c);
        		curr.children.add(next);
        	}
            // Move the curr pointer to the
            // newly created node
            curr = next;
        }

        // Mark the end of the word
        curr.correspondingID = id;
    }
	    
	public List<Integer> search(String key, int maxResults)
    {
        // Initialize the curr pointer with the root node
        TrieNode curr = root; 
        
        char[] arr = key.toCharArray();
        
        //walks down as far as possible;
        for (int i = 0; i < arr.length;i++) {
        	TrieNode next = null;
        	for (TrieNode child : curr.children) {
        		if (child.character==arr[i]) {
        			next = child;
        			break;
        		}
        	}
        	if (next == null) {
        		return null;
        	}
        	curr = next;
        }
        
        //returns the next n values, BFS
        List<Integer> result = new ArrayList<Integer>();
        Queue<TrieNode> queue = new LinkedList<TrieNode>();
        
        while (maxResults > 0 && curr != null) {
        	queue.addAll(curr.children);
        	if (curr.isEndOfWord()) {
        		maxResults -=1;
        		result.add(curr.correspondingID);
        	}
        	curr = queue.poll();
        }   
        
        return result;
    }
	private class TrieNode {
	    
	    // Array for child nodes of each node
	    List<TrieNode> children;
	    char character;
	    // Used for indicating the end of a string
	    Integer correspondingID;
	    
	    public boolean isEndOfWord() {return correspondingID != null;}

	    // Constructor
	    public TrieNode(char character) {
	      
	    	this.character = character;
	    	correspondingID = null;
	        children = new ArrayList<TrieNode>();
	    }
	    
	  
	}
}
