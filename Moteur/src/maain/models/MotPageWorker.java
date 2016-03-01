package maain.models;

import java.util.LinkedList;
import java.util.Map;

import edu.jhu.nlp.wikipedia.WikiPage;
import maain.utils.Utils;

public class MotPageWorker implements Runnable {

	private static Dictionnaire dico;
	private WikiPage page;
	private Map<String, LinkedList<String>> mapWordPage;
	
	public MotPageWorker(Dictionnaire _dico, WikiPage _page, Map<String, LinkedList<String>> _mapWordPage) {
		dico = _dico;
		page = _page;
		mapWordPage = _mapWordPage;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String pageWords[] = Utils.removePunctuation(page.getWikiText());
		/*
		 * Pour chaque mot de la page
		 * on verifie s'il existe dans le dictionnaire
		 * algo de recherche dichotomique 
		 */
		for(String word : pageWords){
			if( Utils.recherche(word, dico.getSortDataFinal())){
				if(mapWordPage.get(word) == null){// 
					mapWordPage.put(word, new LinkedList<String>());
				}
				else{
					mapWordPage.get(word).add(page.getTitle());
				}
			}
		}

	}
}
