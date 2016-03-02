package maain.models;

import java.io.Serializable;

public class Matrice implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
		return mC;
	}

	public int[] getML() {
		return mL;
	}

	public int[] getMI() {
		return mI;
	}
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
