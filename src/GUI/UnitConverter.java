package GUI;

public class UnitConverter {
	
	public static int[] cmToFeetInches(double cm) {
		int[] retVal = new int[2];
		int inches = (int)Math.round((double)cm/2.54);
		retVal[0] = inches/12;
		retVal[1] = inches%12;
		return retVal;
	}
	
	public static double feetInchesToCm(int feet, int inches) {
		int totalInches = feet * 12 + inches;
		return totalInches * 2.54;
	}
}
