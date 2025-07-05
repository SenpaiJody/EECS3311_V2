package ingredientService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import nutrientScore.NutrientScorer;
import nutrientService.INutrientService;
import nutrientService.NutrientServiceFactory;

public class IngredientService implements IIngredientService{
	private IIngredientDB db;
	
	private TrieNode ingredientSearchTrieRoot;
	
	public IngredientService(IIngredientDB databaseImplementation) {
		db = databaseImplementation;
		initializeSearchTrie();
	}
		@Override
	public String getIngredientName(int ingredientID) {
		return db.getIngredientName(ingredientID);
	}
	@Override
	public List<String> getIngredientNames(List<Integer> ids) {
		return db.getIngredientNames(ids);
	}
	@Override
	public List<Integer> getIngredientMatchingNutrients(Map<Integer, Double> target, int maxResults) {
		//backwards priority queue (actually a minQueue; this is so that the option at the head is the "worst" one and can be popped out to keep the size to maxResults
		PriorityQueue<Map.Entry<Integer,Double>> pq = new PriorityQueue<Map.Entry<Integer,Double>>((pair1, pair2)->{
			return pair1.getValue() - pair2.getValue() > 0 ? -1 : 1;
		});
		
		IIngredientIterator iterator = db.getIterator();
		INutrientService nutrientService = NutrientServiceFactory.getService();
		
		List<Integer> totalIngredientList = new ArrayList<Integer>();
		while (iterator.hasNext()) {
			totalIngredientList.add(iterator.getID());
			iterator.next();
		}
		
		Map<Integer,Map<Integer,Double>> totalNutrientMapList = nutrientService.getNutrientsListPer100g(totalIngredientList);
		
		NutrientScorer scorer = new NutrientScorer();

		for (Map.Entry<Integer,Map<Integer,Double>> entry : totalNutrientMapList.entrySet()) {
			Map<Integer,Double> nutrientMap = entry.getValue();
			
			double score = scorer.scoreLikeness(target, nutrientMap);
			pq.add(Map.entry(entry.getKey(), score));
			if (pq.size() > maxResults)
				pq.poll();
		}		
		List<Integer> retVal = new ArrayList<Integer>();
		while (pq.size() > 0) {
			retVal.add(pq.poll().getKey());
		}
		
		List<Integer> reversed = new ArrayList<Integer>(retVal.size());
		for (int i = retVal.size()-1; i >= 0; i--) {
			reversed.add(retVal.get(i));
		}
		
		return reversed;
	}	
	
	
	
	
	@Override
	public List<Integer> searchIngredientByName(String searchTerm, int maxResults) {
        // Initialize the curr pointer with the root node
        TrieNode curr = ingredientSearchTrieRoot; 
        
        char[] arr = searchTerm.toLowerCase().toCharArray();
        
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

	
	private void initializeSearchTrie() {
		IIngredientIterator iterator = db.getIterator();
		
		ingredientSearchTrieRoot = new TrieNode('.'); //the value of the root does not matter
		
		while (iterator.hasNext()) {	
		      TrieNode curr = ingredientSearchTrieRoot;
		        // Iterate across the length of the string
		        for (char c : iterator.getName().toCharArray()) {
		        	if (c >= 'A' && c <= 'Z')
		        		c = (char)(c + 32);
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
		        curr.correspondingID = iterator.getID();
		        iterator.next();
		    }
	}
}

