package maain.models;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import maain.diskIO.Serialisation;
import maain.tp1.App;
import maain.utils.Utils;
import edu.jhu.nlp.wikipedia.PageCallbackHandler;
import edu.jhu.nlp.wikipedia.WikiPage;
import edu.jhu.nlp.wikipedia.WikiXMLParser;
import edu.jhu.nlp.wikipedia.WikiXMLParserFactory;

public class MotPageWorker implements Runnable {

	private static Dictionnaire dico;
	//private WikiPage page;
	private Map<String, LinkedList<String>> mapWordPage;
	private String url; 
	public MotPageWorker(String url, Dictionnaire _dico, Map<String, LinkedList<String>> _mapWordPage) {
		dico = _dico;
		//page = _page;
		mapWordPage = _mapWordPage;
		this.url = url;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		WikiXMLParser wxsp = WikiXMLParserFactory.getSAXParser(url);

		try {
			wxsp.setPageCallback(new PageCallbackHandler() {

				public void process(WikiPage page) {

					if (!page.isRedirect() && !page.isSpecialPage() && !page.isStub()) {
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
			});
			wxsp.parse();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		long startTime = System.nanoTime();
		Map<String, LinkedList<String>> m = new HashMap<String, LinkedList<String>>();
		new Thread(new MotPageWorker(App.path, new Dictionnaire(10), m))
			.start();
		
		Serialisation.save(m, "motpage.ser");
		 long endTime = System.nanoTime();
	     long elapsedTimeInMillis = TimeUnit.SECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS);
	     System.out.println("Total elapsed time: " + elapsedTimeInMillis + " s");
	}
}
