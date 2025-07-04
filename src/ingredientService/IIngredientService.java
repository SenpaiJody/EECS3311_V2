package ingredientService;
import java.util.List;
import java.util.Map;

//Service for everything that has to do with ingredients; instantiated from the IngredientServiceFactory
public interface IIngredientService {
	//gets the "n best matching names" for a provided search term.
	public List<Integer> searchIngredientByName(String searchTerm, int maxResults);
	//gets an ingredient's name when provided an ID
	public String getIngredientName(int ingredientID);
	//gets a list of ingredient's names when provided a list of iD's
	public List<String> getIngredientNames(List<Integer> ingredientIDs);
	//gets up to `maxResults` of the "best" matching ingredients when provided a nutrient list
	public List<Integer> getIngredientMatchingNutrients(Map<Integer, Double> nutrients, int maxResults);
}
