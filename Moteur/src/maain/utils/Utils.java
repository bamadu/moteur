package maain.utils;

import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import maain.models.Matrice;
import maain.models.Vecteur;

public class Utils {
	public static final double epsilon = 1/1000;
	
	public static String[] removePunctuation(String text){
		//String[] res = (text.replaceAll("[^a-zA-Z0-9\\s]|[dl]'", " ").toLowerCase()).split("\\s+");
		String[] res = new String[1];
		res[0] = "RIEN";
		return res;
	}
	
	public static boolean rechercheDichotomique(String word, String[] dic){
		int low = 0;
		int high = (dic.length) - 1;
		boolean match = false;
		while (low <= high){
			int mid = low + ( high - low) / 2;
			if(word.compareToIgnoreCase(dic[mid]) == 0){
				match = true;
				break;
			} else if(word.compareToIgnoreCase(dic[mid]) < 0){
				high = mid - 1;
			} else if(word.compareToIgnoreCase(dic[mid]) > 0){
				low = mid + 1;
			}
		}
		return match;
	}
	
	/*public static void main(String args[]){
		String[] dic = new String[8];
		String text = "H*#*#)#fhdfdfjdf[\\]~323i4i90>>>....,`344 l'''d' we+-@wqe h'heue l'homme d'academie";
		dic[0] = "apple";
		dic[1] = "banana";
		dic[2] = "cherry";
		dic[3] = "dog";
		dic[4] = "eat";
		dic[5] = "food";
		dic[6] = "good";
		dic[7] = "hotel";
		
		String tab[] = Clean.removePunctuation(text);
		for(String s:tab)
			System.out.println(s);
		
		//Clean.checkDictionnary("hotel",dic);
	}*/

	public static boolean recherche(String word, String[] wordList) {
		// TODO Auto-generated method stub
		for(int i=0; i < wordList.length; i++){
			if(word.equalsIgnoreCase(wordList[i]))
				return true;
		}
		return false;
	}
	
	public static boolean recherche(String word, List<String> wordList) {
		for (String w: wordList) 
			if(word.equalsIgnoreCase(w))
					return true;
		return false;
	}
	
	public static boolean recherche(String word, Collection<String> wordList) {
		// TODO Auto-generated method stub
		for (String w: wordList) 
			if(word.equalsIgnoreCase(w))
					return true;
		return false;
	}
	
	public static String displayDate(){
		Date aujourdhui = new Date();
		DateFormat mediumDateFormat = DateFormat.getDateTimeInstance(
		DateFormat.MEDIUM,
		DateFormat.MEDIUM);
		System.out.println(mediumDateFormat.format(aujourdhui));
		return mediumDateFormat.format(aujourdhui);
	}
	
	public static Vecteur calculatePageRank(Matrice m, Vecteur v){
		Vecteur Po = v;
		Vecteur Pk;
		double gama = 0;
		do{
			Pk = Vecteur.prodTransMatrice(m, Po);
			gama = norme(Pk,Po);
			Po = Pk;
		}
		while(gama < epsilon);
		return Pk;
	}
	
	private static double norme(Vecteur pk, Vecteur po) {
		// TODO Auto-generated method stub
		double res = 0;
		for(int i = 0; i < pk.getSize(); i++){
			res += Math.abs(pk.getValue(i) - po.getValue(i));
		}
		return res;
	}

	
	
	
	
	
	
}
