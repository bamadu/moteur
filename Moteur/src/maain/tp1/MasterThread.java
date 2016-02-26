package maain.tp1;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutorService;

import javax.xml.stream.XMLStreamException;

import maain.models.Dictionnaire;
import maain.models.Matrice;
import maain.models.PageHandler;
import maain.models.PageWorker;
import maain.models.Vecteur;
import maain.utils.Utils;

import org.jdom2.JDOMException;

import edu.jhu.nlp.wikipedia.PageCallbackHandler;
import edu.jhu.nlp.wikipedia.WikiPage;
import edu.jhu.nlp.wikipedia.WikiXMLParser;
import edu.jhu.nlp.wikipedia.WikiXMLParserFactory;

public class MasterThread implements PageHandler, Runnable {
	private static Dictionnaire dico ;
	private static Map< String, LinkedList<String>> assocMotPage;
	private static Map<Integer, Vector<String>> idLinks;
	private static Map<String, Integer> idPage;
	private static Vecteur vecteur;
	private Matrice matrice;
	private int id_page = 0;
	private static double C[] = new double[Matrice.MAX_SIZE];
	private static int L[] = new int[Matrice.MAX_SIZE];
	private static int I[] = new int[Matrice.MAX_SIZE];
	private ExecutorService execService;
	private String url;
	public int flag = 0; // 
	public int flagDeb = 0;
	public int ind1 = 0; //
	public int nbPages = 0;
	
	
	
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		parseDocWikiStax(url);
	}
	
	
	public Map< String, LinkedList<String>> getAssocMotPage() {
		// TODO Auto-generated method stub
		return assocMotPage;
	}
	
	
	
	
	public  Map<String, Integer> getIdPage() {
		// TODO Auto-generated method stub
		return idPage;
	}
	
	public  Map<Integer, Vector<String>> getIdLinks() {
		// TODO Auto-generated method stub
		return idLinks;
	}
	
	
	public double[] getC(){
		return C;
	}
	
	public int[] getL(){
		return L;
	}
	
	public int[] getI(){
		return I;
	}
	
	
	public synchronized int getId_page() {
		return id_page++;
	}

	public synchronized void setCandIParameters(String link, int size) {
		C[flag] = (double) (1.0 / (double)size);
		I[flag++] = getIdPage().get(link);
	}
	
	public synchronized void setLParameters() {
		L[ind1] = flagDeb ;
		L[ind1+1] = flag ;
		ind1++;
		flagDeb = flag ;
	}
	/*public void addIdToLink (String link) {
		if(! idPage.values().contains(links)){
			 tmpId = id_page++;
   		 idPage.put(links, tmpId);
   	 } 
	}*/
	
	public synchronized int updateIdPage(String title)  {
		int curPageId; 
		boolean res = idPage.values().contains(title);
		if(! res){
			curPageId = getId_page();
   		 	idPage.put(title, curPageId);
   	 	} else {
   	 		curPageId = idPage.get(title); 
   	 	}
		return curPageId;
	}
	
	//public MasterThread(String url, int maxThreads) throws XMLStreamException, JDOMException, IOException{
	public MasterThread(ExecutorService execService, String url) throws XMLStreamException, JDOMException, IOException{
		//assocMotPage = new HashMap<String, LinkedList<String>>();
		assocMotPage = Collections.synchronizedMap(new HashMap<String, LinkedList<String>>());
		//idPage = new HashMap<String, Integer>();
		idPage = Collections.synchronizedMap(new HashMap<String, Integer>());
		//idLinks = new HashMap<Integer, Vector<String>>();
		idLinks = Collections.synchronizedMap(new HashMap<Integer, Vector<String>>());
		System.out.println("Loading ...");
		
		//dico = new Dictionnaire("https://fr.wiktionary.org/wiki/Wiktionnaire:10000-wp-fr-10000");
		
		/**
		 * get words from the first wikipedia's link, for test
		 * do not forget to set the parameter to 10 after ...
		 */
		this.url = url;
		dico = new Dictionnaire(1); // 
		//execService = Executors.newFixedThreadPool(maxThreads);
		this.execService = execService;
		
		//execService.shutdownNow();
		
	}
	
	
	public void parseDocWikiStax(String url){
		WikiXMLParser wxsp = WikiXMLParserFactory.getSAXParser(url);
		
	    try {
	        wxsp.setPageCallback(new PageCallbackHandler() { 
	        	
	             public void process(WikiPage  page) {
	            	 //System.out.println("Id : "+Thread.currentThread().getId());
	            	 /* à remettre dans MasterThread */
	         		if(page.isRedirect() ||page.isSpecialPage() || page.isStub()){
	             		 return;
	             	 }
	         		// sinon lancer le PageWorker ...
	         		try {
						MasterThread.this.queueLink(page);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

	         		nbPages ++;
	             }
	        });
	       wxsp.parse();
	    }catch(Exception e) {
	            e.printStackTrace();
	    }
	    
	    matrice = new Matrice(C, L, I);
	    /*for(int i=0; i<matrice.getMC().length && matrice.getMC()[i]!=0;i++){
 	    	System.out.print(matrice.getMC()[i]+", ");
 	    }*/
 	    System.out.println("\n nbPage : "+nbPages+", 1/nbPages : "+ (1.0/nbPages));
 	    /**
	     * Ici commencer le produit
	     */
	    vecteur = new Vecteur(Matrice.MAX_SIZE, (double) (1.0/nbPages)); // nbPages nombre de noeuds 
	    Vecteur vectResultat = Utils.calculatePageRank(matrice, vecteur);
	    Vecteur.displayVector(vectResultat, nbPages);
	    /**
	     * Ici commencer le produit
	     */
	}
	
	/**
	 * 
	 * @param page une page wikipedia
	 * @param map relation mot page
	 * 
	 *
	public static void fillMotPageRelation(String pageWords[], String titre, Map<String, LinkedList<String>> map){
		//String pageWords[] = Clean.removePunctuation(page.getContenu());
	    /*
	     * Pour chaque mot de la page
	     * on verifie s'il existe dans le dictionnaire
	     * algo de recherche dichotomique 
	     *
	    for(String word : pageWords){
	    	if( Utils.recherche(word, dico.getSortDataFinal())){
	    		if(map.get(word) == null){// 
	    			map.put(word, new LinkedList<String>());
	    		}
	    		else{
	    			map.get(word).add(titre);
	    		}
	    	}
	    }
	}*/
	
	private void startNewThread(WikiPage page) throws Exception {
        execService.execute(new PageWorker(page, this));
    }
	
	@Override
	public void queueLink(WikiPage page) throws Exception {
		// TODO Auto-generated method stub
		startNewThread(page);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return idPage.size();
	}

	@Override
	public boolean visited(WikiPage page) {
		// TODO Auto-generated method stub
		return idPage.containsKey(page.getTitle());
	}

	@Override
	public void addVisited(WikiPage page) {
		// TODO Auto-generated method stub
		
	}

	public Dictionnaire getDico() {
		return dico;
	}
	
	
}
