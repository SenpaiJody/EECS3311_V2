package ingredientService;

import java.util.ArrayList;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import nutrientScore.NutrientScorer;
import nutrientScore.INutrientScorer;

import nutrientService.INutrientService;
import nutrientService.NutrientServiceFactory;
import recommendation.GoalType;

/**An implementation of IIngredientService that: 
 * <ul>
 * <li>Uses an IIngredientDB implementation to obtain data</li>
 * <li>Uses a TrieTree to store the names</li>
 * </ul>
 * 
 * This class essentially adds logic between the underlying IIngredientDB and the IIngredientService interface.
 * <p> This allows the IUserDB to focus more on CRUD actions while still fulfilling the IIngredientService interface.
 * */
public class IngredientService implements IIngredientService{

	private IIngredientDB db;
	
	private TrieNode ingredientSearchTrieRoot;
	
	/*Constructor; initializes the searchTrie*/
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
	
	/* Implementation using a Priority Queue and an IIngredientIterator to iterate through every ingredient and process it,
	 * saving the best 'maxResult' entries and returning them
	 * 
	 * */
	// Interface method implementation - handles lists of nutrients and goals
	@Override
	public List<Integer> getIngredientMatchingNutrients(Map<Integer, Double> target, int maxResults, 
	                                                   List<Integer> nutrientID, List<GoalType> type) {
	    // Validation: ensure both lists have the same size
	    if (nutrientID != null && type != null && nutrientID.size() != type.size()) {
	        throw new IllegalArgumentException("nutrientID and type lists must have the same size");
	    }
	    
	    //backwards priority queue (actually a minQueue; this is so that the option at the head is the "worst" one and can be quickly popped out to keep the size to maxResults
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

	    INutrientScorer scorer = new NutrientScorer();

	    for (Map.Entry<Integer,Map<Integer,Double>> entry : totalNutrientMapList.entrySet()) {
	        Map<Integer,Double> nutrientMap = entry.getValue();

	        // Filter: Skip scoring if any of the specific nutrients don't meet their goal criteria
	        boolean skipIngredient = false;
	        
	        if (nutrientID != null && type != null) {
	            for (int i = 0; i < nutrientID.size(); i++) {
	                Integer currentNutrientID = nutrientID.get(i);
	                GoalType currentGoalType = type.get(i);
	                
	                double targetValue = target.get(currentNutrientID);
	                double trialValue = nutrientMap.get(currentNutrientID);
	                
	                if (currentGoalType == GoalType.DECREASE) {
	                    // For DECREASE: we want trial < target (ingredient has less of this nutrient)
	                    if (trialValue > targetValue) {
	                        skipIngredient = true;
	                        break; // Exit the loop early if any condition fails
	                    }
	                } else if (currentGoalType == GoalType.INCREASE) {
	                    // For INCREASE: we want trial > target (ingredient has more of this nutrient)  
	                    if (trialValue < targetValue) {
	                        skipIngredient = true;
	                        break; // Exit the loop early if any condition fails
	                    }
	                }
	            }
	        }
	        
	        if (skipIngredient) {
	            continue; // Skip this ingredient if it doesn't meet any of the goal criteria
	        }

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
	
	// Helper method for single nutrient/goal (if you still need backward compatibility)
	public List<Integer> getIngredientMatchingNutrients(Map<Integer, Double> target, int maxResults, 
	                                                   Integer singleNutrientID, GoalType singleType) {
	    if (singleNutrientID == null || singleType == null) {
	        return getIngredientMatchingNutrients(target, maxResults, (List<Integer>)null, (List<GoalType>)null);
	    }
	    
	    List<Integer> nutrientList = new ArrayList<>();
	    nutrientList.add(singleNutrientID);
	    
	    List<GoalType> typeList = new ArrayList<>();
	    typeList.add(singleType);
	    
	    return getIngredientMatchingNutrients(target, maxResults, nutrientList, typeList);
	}
	
	
	
	/* An implementation using a searchTrie (initialized on construction) to
	 * search for the best ingredient that matches the name.
	 * Performs fairly quickly.
	 * */
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

	
	/** The node of a TrieTree structure for searching ingredient names
	 * 
	 * Adapted from (https://www.geeksforgeeks.org/dsa/trie-insert-and-search/)
	 * */
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

	
	/* Initializes the searchTrie with all of the ingredient names. While this does end up "storing" all of the ingredient names
	 * in memory, due to the nature of a TrieTree, they are stored efficiently without taking up too much memory
	 * */
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
	@Override
	public int getFoodGroup(int ingredientID) {
		return db.getFoodGroup(ingredientID);
	}
	@Override
	public String getFoodGroupName(int foodGroupID) {
		return db.getFoodGroupName(foodGroupID);
	}
}

