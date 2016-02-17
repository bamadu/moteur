package maain.tp1;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import javax.xml.stream.XMLStreamException;

import org.jdom2.JDOMException;

import edu.jhu.nlp.wikipedia.PageCallbackHandler;
import edu.jhu.nlp.wikipedia.WikiPage;
import edu.jhu.nlp.wikipedia.WikiXMLParser;
import edu.jhu.nlp.wikipedia.WikiXMLParserFactory;
import maain.models.Dictionnaire;
import maain.models.Matrice;
import maain.models.Vecteur;
import maain.utils.Utils;

public class Parse {
	private static Dictionnaire dico ;
	private static HashMap< String, LinkedList<String>> assocMotPage;
	private static HashMap<Integer, Vector<String>> idLinks;
	private static HashMap<Integer, String> idPage;
	private static Vecteur vecteur;
	private Matrice matrice;
	
	
	public Parse(String url) throws XMLStreamException, JDOMException, IOException{
		assocMotPage = new HashMap<String, LinkedList<String>>();
		idPage = new HashMap<Integer, String>();
		idLinks = new HashMap<Integer, Vector<String>>();
		System.out.println("Loading ...");
		
		//dico = new Dictionnaire("https://fr.wiktionary.org/wiki/Wiktionnaire:10000-wp-fr-10000");
		dico = new Dictionnaire(1);
		parseDocWikiStax(url);
		
	}
	
	int ind1 = 0;
	int ii = 0;
	int curPageId = 0;
	int flag = 0;
	int flagDeb = 0;
	String tmpTitle;
	int tmpId;
	Vector<String> tmpLinks;
	int nbPages = 0;
	
	public void parseDocWikiStax(String url){
		WikiXMLParser wxsp = WikiXMLParserFactory.getSAXParser(url);
		float C[] = new float[Matrice.MAX_SIZE];
		int L[] = new int[Matrice.MAX_SIZE];
	    try {
	        wxsp.setPageCallback(new PageCallbackHandler() { 
	             public void process(WikiPage  page) {
	            	 if(page.isRedirect() ||page.isSpecialPage() || page.isStub()){
	            		//System.out.println("<<<<<<<<<<<  Page anormale ....");
	            		 return;
	            	 }
	            	 curPageId = ii++;
	            	 tmpTitle = page.getTitle();
	            	 if(! idPage.values().contains(tmpTitle)){
	            		 idPage.put((curPageId), tmpTitle);
	            	 }
	            	 
	            	 tmpLinks = page.getLinks();
	            	 for(String links : tmpLinks){
	            		 if(! idPage.values().contains(links)){
	            			 tmpId = ii++;
		            		 idPage.put((tmpId), links);
		            	 } 
	            		 C[flag++] = (float) (1.0 / (float)tmpLinks.size()); 
	            	 }
	            	 float cpt = 0;
	         	     for(int i=flagDeb; i<flag && C[i]!=0;i++){
	         	    	System.out.print(C[i]+", ");
	         	    	cpt += C[i];
	         	    }
	         	    System.out.println("\n<<>"+cpt);
	         	    
	            	 L[ind1] = flagDeb ;
	            	 L[ind1++] = flag ;
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
	    matrice = new Matrice(C, L, null);
	    vecteur = new Vecteur(nbPages);
	    /**
	     * Ici commencer
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
	    	//System.out.println(word);
	    	//if( Clean.recherche(word, dico.getWordList())){
	    	if( Utils.recherche(word, dico.getSortDataFinal())){
	    		//System.out.println("Find "+word+" ---> "+page.getTitre());
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
}
