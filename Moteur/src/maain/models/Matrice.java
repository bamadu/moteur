package maain.models;

public class Matrice {
	private int m;
	private int size;
	private double mC[];
	private int mL[];
	private int mI[];
	public static int MAX_SIZE = 15000000;
	
	public Matrice(int size, int nb){
		this.size = size;
		m = nb;
		mC = new double[m];
		mL = new int[size+1];
		mI = new int[m];
	}

	public Matrice (double mC[], int mL[], int mI[]) {
		this.mC = mC;
		this.mI = mI;
		this.mL = mL;
	}
	public double[] getMC() {
		// TODO Auto-generated method stub
		return mC;
	}

	public int[] getML() {
		// TODO Auto-generated method stub
		return mL;
	}

	public int[] getMI() {
		// TODO Auto-generated method stub
		return mI;
	}
}
