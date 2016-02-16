package matrices;

public class Matrice {
	private int m;
	private int n;
	private float mC[];
	private int mL[];
	private int mI[];
	public static int MAX_SIZE = 15000000;
	
	public Matrice(int size, int nb){
		this.n = size;
		m = nb;
		mC = new float[m];
		mL = new int[n+1];
		mI = new int[m];
	}

	public Matrice (float mC[], int mL[], int mI[]) {
		this.mC = mC;
		this.mI = mI;
		this.mL = mL;
	}
	public float[] getMC() {
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
