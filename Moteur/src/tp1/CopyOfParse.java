package tp1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;

import edu.jhu.nlp.wikipedia.PageCallbackHandler;
import edu.jhu.nlp.wikipedia.WikiPage;
import edu.jhu.nlp.wikipedia.WikiXMLParser;
import edu.jhu.nlp.wikipedia.WikiXMLParserFactory;

public class CopyOfParse {
	private static Dictionnaire dico ;
	private static HashMap< String, LinkedList<String>> assocMotPage;
	private static HashMap<Integer, Vector<String>> idLinks;
	private static HashMap<Integer, String> idPage;
	private static int ieme = 1;
	
	
	public CopyOfParse(String url) throws XMLStreamException, JDOMException, IOException{
		assocMotPage = new HashMap<String, LinkedList<String>>();
		idPage = new HashMap<Integer, String>();
		System.out.println("Loading ...");
		
		dico = new Dictionnaire("https://fr.wiktionary.org/wiki/Wiktionnaire:10000-wp-fr-10000");
		//parseDoc(url);
		//parseDocStax(url);
		parseDocWikiStax(url);
		
	}
	
	long ii = 0;
	String tmpTitle;
	int tmpId;
	Vector<String> tmpLinks;
	
	public void parseDocWikiStax(String url){
		WikiXMLParser wxsp = WikiXMLParserFactory.getSAXParser(url);
		System.out.println("here");
	    try {
	        wxsp.setPageCallback(new PageCallbackHandler() { 
	             public void process(WikiPage page) {
	            	 if(page.isRedirect() ||page.isSpecialPage() || page.isStub()){
	            		System.out.println("<<<<<<<<<<<  Page anormale ....");
	            		 return;
	            	 }
	            	 /*tmpTitle = page.getTitle();
	            	 tmpId = Integer.parseInt(page.getID());
	            	 tmpLinks = page.getLinks();*/
	            	 
	            	 ii++;
	            	// System.out.println(page.getWikiText()+" ,"+"ii = "+ii);
	            	 
	            	 /* association mot page (à revoir) */
	            	/* fillMotPageRelation(Clean.removePunctuation(page.getWikiText()), tmpTitle, assocMotPage);
	            	 idPage.put((tmpId), tmpTitle);
	            	 idLinks.put(tmpId, tmpLinks);*/
	             }
	        });
	       wxsp.parse();
	    }catch(Exception e) {
	            e.printStackTrace();
	    }
	}
	
	/**
	 * Parcours ligne par ligne un fichier xml
	 * @param url chemin du fichier xml
	 * @throws FileNotFoundException
	 * @throws XMLStreamException
	 */
	public void parseDocStax(String url) throws FileNotFoundException, XMLStreamException{
		XMLInputFactory factory = XMLInputFactory.newInstance();
		File file = new File(url);
		XMLStreamReader reader = factory.createXMLStreamReader(new FileReader(file));
		
		while (reader.hasNext()) {
		    // Récupération du type de l'élement
		    int type = reader.next();
		   
		    switch (type) {
		        case XMLStreamReader.START_ELEMENT:
		            // Si c'est un début de balise user, alors on lance le traitement d'un utilisateur
		            if ("page".equals(reader.getLocalName())){
		                processPage(reader);
		            }
		            break;
		    }
		}

	}
	/**
	 * @param reader flux de lecture de fichier XML
	 * @throws XMLStreamException
	 * Met à jour la relation mot page
	 * La matrice CLI(pas encore fait)
	 */
	private void processPage(XMLStreamReader reader) throws XMLStreamException {
		// TODO Auto-generated method stub
		int flag = 0;
	    final int FLAG_TEXT = 1;
	    final int FLAG_TITLE = 2;
	    final int FLAG_NONE = 0;
	    boolean state = true;
	    String contenuText = "";
	    String titre = "";
	    Page page = new Page();
	    
	    while (reader.hasNext() && state) {
	        int type = reader.next();

	        switch (type) {
	            // Si c'est un début d'elément, on garde son type
	            case XMLStreamReader.START_ELEMENT:
	                if (reader.getLocalName().equals("text"))
	                    flag = FLAG_TEXT;
	                else if (reader.getLocalName().equals("title"))
	                    flag = FLAG_TITLE;
	                else 
	                	flag = FLAG_NONE;
	                break;
	            // Si c'est du texte ou une zone CDATA ...
	            case XMLStreamReader.CDATA:
	            case XMLStreamReader.CHARACTERS:
	                switch (flag) {
	                    case FLAG_TITLE:
	                        // et que ce n'est pas une chaine de blanc
	                        if(!(reader.isWhiteSpace())){
	                        	titre = reader.getText();     	
	                        }
	                        	
	                        break;
	                    case FLAG_TEXT:
	                        // et que ce n'est pas une chaine de blanc
	                        if(! (reader.isWhiteSpace())){
	                        	contenuText += reader.getText();
	                        }
	                        	//
	                        break;
	                }
	                break;
	            case XMLStreamReader.END_ELEMENT:
	                // Si c'est la fin de la balise user, on change l'indicateur de boucle
	                if (reader.getLocalName().equals("page")) 
	                	state = false;
	                break;
	        }
	    }
	    
	    idPage.put(ieme++, titre); // On associe la page à son identité
	    page.setTitre(titre);
	    page.setContenu(contenuText);// texte non nettoyé
	    String pageWords[] = Clean.removePunctuation(page.getContenu());
	    fillMotPageRelation(pageWords, titre, assocMotPage);
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
	    	if( Clean.recherche(word, dico.getWordList())){
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
	

	public void parseDoc(String url) throws JDOMException, IOException{
		File input = new File(url);

		SAXBuilder saxBuilder = new SAXBuilder();
		Document doc = saxBuilder.build(input);
		Iterator<?> processDescendants = doc.getDescendants(new ElementFilter("page"));
		int id = 0;
		Page page;
		LinkedList<Page> pageList = new LinkedList<Page>();
		while(processDescendants.hasNext()) {
			Element e =  (Element) processDescendants.next();
			Iterator<?> descendants = e.getDescendants(new ElementFilter("text"));
			
			while(descendants.hasNext()) {
				Element ebis = (Element)descendants.next();
				page = new Page();
				page.setContenu(ebis.getText());
				for(Element elt : e.getChildren()){
					if(elt.getName().equals("title")){
						page.setTitre(elt.getText());
					}
				}
				page.setId(++id);
				pageList.add(page);
				break; // on s'attend juste à un element text 
			}
		}
	}

	public static String displayDate(){
		Date aujourdhui = new Date();
		DateFormat mediumDateFormat = DateFormat.getDateTimeInstance(
		DateFormat.MEDIUM,
		DateFormat.MEDIUM);
		System.out.println(mediumDateFormat.format(aujourdhui));
		return mediumDateFormat.format(aujourdhui);
	}
	@SuppressWarnings("unused")
	public static void main(String args[]) throws XMLStreamException, JDOMException, IOException{
		//String deb = displayDate();
		 //Parse parse = new Parse("pagetest.xml");
		
		CopyOfParse parse = new CopyOfParse("/Users/Sacko/Documents/Master/Master_2/Semestre_2/MAIN/MesTPS/frwiki.xml");
		//displayLinkOfPage("if[[Moulins (Allier)|Moulins]][[Coulines (Pallier)|Moulins]] [[Moulins (Allier)|Moulins]]ret", null);
		//System.out.println("deb : "+deb+", fin : "+displayDate());
		for(int i = 0; i <= 3000000; i++) System.out.println("tot");;
		System.out.println("FIN!");
	}
}
