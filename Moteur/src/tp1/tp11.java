package tp1;

import java.text.Normalizer;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Collections;
import java.util.List;


public class tp11 {
	private static final int WORDNUMBER = 1000;
	private String[] wordList = new String[WORDNUMBER];
	private List<String> sortDataFinal = new ArrayList<String>();
	
	public tp11(String url){ // constructeur
		
		sortDataFinal = cleanData(getData(url));
		for(String sortDataFinals : sortDataFinal ){
			System.out.println(sortDataFinals);
		}
		
	}
	
	private String[] getData(String url){ // recuperation de données
		
		try {
			Document doc = Jsoup.connect(url).get();
			Elements ul = doc.select("div#mw-content-text > ul");
			Elements li = ul.select("li");
			
			for (int i = 0; i < li.size(); i++) {
			    wordList[i] = li.get(i).select("a").text();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return wordList;
		
	}
	
	private List<String> cleanData(String[] list){ // nettoyage de données et trier
		
		List<String> sortData = new ArrayList<String>();
		for(int i=0; i<WORDNUMBER; i++){
			sortData.add(replaceChar(removeAccents(list[i]).toLowerCase()));
		}
		Collections.sort(sortData);
		return sortData;
		
	}
	
	private static String removeAccents(String s){ // enlevement d'accent
	    s = Normalizer.normalize(s, Normalizer.Form.NFD);
	    s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
	    return s;
	}
	
	private String replaceChar(String s){
		 String firstCaract = "l'";
		 String replaceWith = "@";
		 String temp = s.replace(firstCaract,replaceWith);
	     return temp;
	}
	
	public static void main(String args[]){
		
		String url = "https://fr.wiktionary.org/wiki/Wiktionnaire:10000-wp-fr-10000";
		tp11 tp11 = new tp11(url);
		
	}

}
