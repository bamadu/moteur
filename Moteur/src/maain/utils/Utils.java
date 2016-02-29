package maain.utils;

import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import maain.models.Matrice;
import maain.models.Vecteur;

public class Utils {
	public static final double epsilon = (double) 1.0/1000.0;
	
	public static String[] removePunctuation(String text){
		return (text.replaceAll("[^a-zA-Z0-9\\s]|[dl]'", " ")
				.toLowerCase())
				.split("\\s+");
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

	public static boolean recherche(String word, String[] wordList) {
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
		double res = 0;
		for(int i = 0; i < pk.getSize(); i++)
			res += Math.abs(pk.getValue(i) - po.getValue(i));
		return res;
	}
	
}
