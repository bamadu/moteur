package maain.models;

import java.io.Serializable;

public class Vecteur implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	double tab[];
	int size;
	public Vecteur(int n){
		this.size = n;
		tab = new double[n];
	}
	
	public double[]getTab(){
		return tab;
	}
	
	
	public Vecteur(int n, double value){
		this.size = n;
		tab = new double[n];
		for(int i = 0; i < tab.length; i++)
			tab[i] = value;
	}
	
	public Vecteur(double []t){
		tab = t;
		size = t.length;
	}
	
	public int getSize(){
		return size;
	}
	
	public double getValue(int i){
		return tab[i];
	}
	
	public static Vecteur prodMatrice(Matrice mat, Vecteur vector){
		double vectTab[] = vector.tab; 
		double []resultat = new double[vectTab.length];
		double []mC = mat.getMC();
		int []mI = mat.getMI();// mC et mI 
		int []mL = mat.getML();
		
		for (int j=1, i=0; j<mL.length; i++, j++){
			for(int k=mL[i]; k<mL[j]; k++) {
				resultat[i] += mC[k]*vectTab[mI[k]];
			}
		}
		return new Vecteur(resultat);
	}
	
	public static Vecteur prodTransMatrice(Matrice mat, Vecteur vector){
		double vectTab[] = vector.tab; 
		double []resultat = new double[vectTab.length];
		double []mC = mat.getMC();
		int []mI = mat.getMI();// mC et mI 
		int []mL = mat.getML();
		
		for (int j=1, i=0, ind=0; j<mL.length; i++, j++){
			for(int k=mL[i]; k<mL[j]; k++, ind++) {
				resultat[mI[ind]] += mC[k]*vectTab[i];
			}
		}
		System.out.println();
		return new Vecteur(resultat);
	}
	
	public static void displayVector( Vecteur v, int size) {
		System.out.print("[ ");
		int i =0;
		for (double e: v.tab){
			System.out.print(e+" ");
			if((++i) == size+20)
				break;
		}
		System.out.println(" ]");
	}
	
	public static void main(String[] args) {
		double []v = {2, 5, 7, 8};
		double []mC = {1, 2, 3, 4, 5, 6};
		int []mI = {0, 3, 1, 2, 3, 0}; 
		int []mL = {0, 2, 2, 5, 6};
		Matrice m = new Matrice(mC, mL, mI);
		
		Vecteur vec = prodMatrice(m, new Vecteur(v));
		displayVector(vec, v.length);
	}

}
