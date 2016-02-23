package maain.tp1;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import javax.xml.stream.XMLStreamException;

import maain.models.Dictionnaire;
import maain.models.Matrice;
import maain.models.PageHandler;
import maain.models.Vecteur;
import maain.utils.Utils;

import org.jdom2.JDOMException;

import edu.jhu.nlp.wikipedia.PageCallbackHandler;
import edu.jhu.nlp.wikipedia.WikiPage;
import edu.jhu.nlp.wikipedia.WikiXMLParser;
import edu.jhu.nlp.wikipedia.WikiXMLParserFactory;

public class MasterThread implements PageHandler {
	private static Dictionnaire dico ;
	private static HashMap< String, LinkedList<String>> assocMotPage;
	private static HashMap<Integer, Vector<String>> idLinks;
	private static HashMap<String, Integer> idPage;
	private static Vecteur vecteur;
	private Matrice matrice;
	
	public MasterThread(String url) throws XMLStreamException, JDOMException, IOException{
		assocMotPage = new HashMap<String, LinkedList<String>>();
		idPage = new HashMap<String, Integer>();
		idLinks = new HashMap<Integer, Vector<String>>();
		System.out.println("Loading ...");
		
		//dico = new Dictionnaire("https://fr.wiktionary.org/wiki/Wiktionnaire:10000-wp-fr-10000");
		/**
		 * get words from the first wikipedia's link, for test
		 * do not forget to set the parameter to 10 after ...
		 */
		dico = new Dictionnaire(1); //  
		parseDocWikiStax(url);
		
	}
	
	int nbPages = 0;
	public void parseDocWikiStax(String url){
		WikiXMLParser wxsp = WikiXMLParserFactory.getSAXParser(url);
		double C[] = new double[Matrice.MAX_SIZE];
		int L[] = new int[Matrice.MAX_SIZE];
		int I[] = new int[Matrice.MAX_SIZE];
	    try {
	        wxsp.setPageCallback(new PageCallbackHandler() { 
	        	int ind1 = 0;
	    		int ii = 0;
	    		int curPageId = 0;
	    		int flag = 0;
	    		int flagDeb = 0;
	    		String tmpTitle;
	    		int tmpId;
	    		Vector<String> tmpLinks;
	    		
	             public void process(WikiPage  page) {
	            	 System.out.println("Id : "+Thread.currentThread().getId());
	            	 if(page.isRedirect() ||page.isSpecialPage() || page.isStub()){
	            		//System.out.println("<<<<<<<<<<<  Page anormale ....");
	            		 return;
	            	 }
	            	 curPageId = ii++;
	            	 tmpTitle = page.getTitle().toLowerCase();
	            	 if(! idPage.values().contains(tmpTitle)){
	            		 idPage.put(tmpTitle, curPageId);
	            	 }
	            	 /**
	            	  * Prendre en compte la casse des titres
	            	  */
	            	 tmpLinks = page.getLinks();
	            	 for(String links : tmpLinks){
	            		 links = links.toLowerCase();// casse minuscule
	            		 if(! idPage.values().contains(links)){
	            			 tmpId = ii++;
		            		 idPage.put(links, tmpId);
		            	 } 
	            		 C[flag] = (double) (1.0 / (double)tmpLinks.size()); 
	            		 I[flag++] = idPage.get(links); 
	            		 
	            	 }
	            	 /*double cpt = 0;
	         	     for(int i=flagDeb; i<flag && C[i]!=0;i++){
	         	    	System.out.print(C[i]+", ");
	         	    	cpt += C[i];
	         	    }
	         	    System.out.println("\n<<>"+cpt);*/
	         	    
	            	 L[ind1] = flagDeb ;
	            	 L[ind1+1] = flag ;
	            	 ind1++;
	            	 flagDeb = flag ;
	            	 /* association mot page (à revoir) */
	            	 fillMotPageRelation(Utils.removePunctuation(page.getWikiText()), tmpTitle, assocMotPage);
	            	 idLinks.put(tmpId, tmpLinks);
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
	 * @param assocMP relation mot page
	 * 
	 */
	public static void fillMotPageRelation(String pageWords[], String titre, HashMap<String, LinkedList<String>> assocMP){
		//String pageWords[] = Clean.removePunctuation(page.getContenu());
	    /*
	     * Pour chaque mot de la page
	     * on verifie s'il existe dans le dictionnaire
	     * algo de recherche dichotomique 
	     */
	    for(String word : pageWords){
	    	if( Utils.recherche(word, dico.getSortDataFinal())){
	    		if(assocMP.get(word) == null){// 
	    			assocMP.put(word, new LinkedList<String>());
	    		}
	    		else{
	    			assocMP.get(word).add(titre);
	    		}
	    	}
	    }
	}

	
	@SuppressWarnings("unused")
	public static void main(String args[]) throws XMLStreamException, JDOMException, IOException{
		
	}

	@Override
	public void queueLink(WikiPage page) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean visited(WikiPage page) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addVisited(WikiPage page) {
		// TODO Auto-generated method stub
		
	}
}
