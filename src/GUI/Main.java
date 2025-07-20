package GUI;

import nutrientService.INutrientIterator;
import nutrientService.INutrientService;
import nutrientService.NutrientServiceFactory;

public class Main {

	
	
	public static void main(String[] args) {

//		INutrientService nutrientService = NutrientServiceFactory.getService();
//		INutrientIterator iterator = nutrientService.getIterator();
//		while (true) {
//			
//			//do whatever you need to do....
//			System.out.println(iterator.getIngredientID() + ": ");
//			StringBuilder sb = new StringBuilder();
//			iterator.getNutrientMap().forEach((id, amt)->{
//				sb.append(String.format("  %d : %.2f\n", id, amt));
//			});
//			System.out.println(sb.toString());
//			//do whatever you need to do....
//			
//			
//			//check if the iterator has another value
//			if (!iterator.hasNext())
//				break;
//			//update the iterator's state
//			iterator.next();
//		}
//		
		MainWindow mainWindow = new MainWindow();
		
		
	}
}
