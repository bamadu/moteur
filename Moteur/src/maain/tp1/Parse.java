package maain.tp1;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import javax.xml.stream.XMLStreamException;

import maain.models.Dictionnaire;
import maain.models.Matrice;
import maain.models.Vecteur;
import maain.utils.Utils;

import org.jdom2.JDOMException;

import edu.jhu.nlp.wikipedia.PageCallbackHandler;
import edu.jhu.nlp.wikipedia.WikiPage;
import edu.jhu.nlp.wikipedia.WikiXMLParser;
import edu.jhu.nlp.wikipedia.WikiXMLParserFactory;

public class Parse {
	private static Dictionnaire dico ;
	private static HashMap< String, LinkedList<String>> assocMotPage;
	private static HashMap<Integer, Vector<String>> idLinks;
	private static HashMap<String, Integer> idPage;
	private static Vecteur vecteur;
	private Matrice matrice;
	public static final float epsilon = 1/1000;
	
	
	public Parse(String url) throws XMLStreamException, JDOMException, IOException{
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
		float C[] = new float[Matrice.MAX_SIZE];
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
	    		int col = 0;
	             public void process(WikiPage  page) {
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
	            		 C[flag++] = (float) (1.0 / (float)tmpLinks.size()); 
	            		 I[col++] = idPage.get(links); 
	            		 
	            	 }
	            	 /*float cpt = 0;
	         	     for(int i=flagDeb; i<flag && C[i]!=0;i++){
	         	    	System.out.print(C[i]+", ");
	         	    	cpt += C[i];
	         	    }
	         	    System.out.println("\n<<>"+cpt);*/
	         	    
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
	    matrice = new Matrice(C, L, I);
	    vecteur = new Vecteur(Matrice.MAX_SIZE, 1/nbPages); // nbPages nombre de noeuds 
	    Vecteur vectResultat = calculatePageRank(matrice, vecteur);
	    Vecteur.displayVector(vectResultat);
	    /**
	     * Ici commencer le produit
	     */
	}
	
	
	public static Vecteur calculatePageRank(Matrice m, Vecteur v){
		Vecteur Po = v;
		Vecteur Pk;
		float gama = 0;
		do{
			Pk = Vecteur.prodTransMatrice(m, Po);
			gama = norme(Pk,Po);
			Po = Pk;
		}
		while(gama < epsilon);
		return Pk;
	}
	
	private static float norme(Vecteur pk, Vecteur po) {
		// TODO Auto-generated method stub
		float res = 0;
		for(int i = 0; i < pk.getSize(); i++){
			res += Math.pow(Math.abs(pk.getValue(i) - po.getValue(i)), 2);
		}
		return (float) Math.sqrt(res);
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
