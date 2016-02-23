package maain.models;

public class Vecteur {
	float tab[];
	int size;
	public Vecteur(int n){
		this.size = n;
		tab = new float[n];
	}
	
	public Vecteur(int n, float value){
		this.size = n;
		tab = new float[n];
		for(int i = 0; i < tab.length; i++)
			tab[i] = value;
	}
	
	public Vecteur(float []t){
		tab = t;
		size = t.length;
	}
	
	public int getSize(){
		return size;
	}
	
	public float getValue(int i){
		return tab[i];
	}
	
	public static Vecteur prodMatrice(Matrice mat, Vecteur vector){
		float vectTab[] = vector.tab; 
		float []resultat = new float[vectTab.length];
		float []mC = mat.getMC();
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
		float vectTab[] = vector.tab; 
		float []resultat = new float[vectTab.length];
		float []mC = mat.getMC();
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
	
	public static void displayVector( Vecteur v) {
		System.out.print("[ ");
		for (float e: v.tab)
			System.out.print(e+" ");
		System.out.println(" ]");
	}
	
	public static void main(String[] args) {
		float []v = {2, 5, 7, 8};
		float []mC = {1, 2, 3, 4, 5, 6};
		int []mI = {0, 3, 1, 2, 3, 0}; 
		int []mL = {0, 2, 2, 5, 6};
		Matrice m = new Matrice(mC, mL, mI);
		
		Vecteur vec = prodMatrice(m, new Vecteur(v));
		displayVector(vec);
	}

}
