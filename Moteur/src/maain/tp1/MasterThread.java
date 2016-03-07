package maain.tp1;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;

import javax.xml.stream.XMLStreamException;

import maain.diskIO.DiskIO;
import maain.diskIO.Serialisation;
import maain.models.Dictionnaire;
import maain.models.Matrice;
import maain.models.Page;
import maain.models.Vecteur;
import maain.utils.Utils;

import org.jdom2.JDOMException;

import edu.jhu.nlp.wikipedia.PageCallbackHandler;
import edu.jhu.nlp.wikipedia.WikiPage;
import edu.jhu.nlp.wikipedia.WikiXMLParser;
import edu.jhu.nlp.wikipedia.WikiXMLParserFactory;

public class MasterThread implements Runnable {

	private static Dictionnaire dico;
	private static Map<String, LinkedList<String>> assocMotPage;
	private static Map<String, LinkedList<Page>> assocMotPageRank;
	private static Map<String, Integer> idPage;
	private static Vecteur vecteur;
	private Matrice matrice;
	private int id_page = 0;
	private static double C[] = new double[Matrice.MAX_SIZE];
	private static int L[] = new int[Matrice.MAX_SIZE];
	private static int I[] = new int[Matrice.MAX_SIZE];
	private int numberOfPageWorker = 0;
	private int pageWorkerDone = 0;
	private int nbPages = 0;
	public int flag = 0; //
	public int flagDeb = 0;
	public int ind1 = 0; //
	private String url;

	public MasterThread(String url) throws XMLStreamException, JDOMException,
			IOException {

		assocMotPage = Collections.synchronizedMap(new HashMap<String, LinkedList<String>>());
		assocMotPageRank = Collections.synchronizedMap(new HashMap<String, LinkedList<Page>>());
		idPage = Collections.synchronizedMap(new HashMap<String, Integer>());
		System.out.println("[masterThread] Loading dictionnaire for wiki ...");

		/**
		 * get words from the first wikipedia's link, for test do not forget to
		 * set the parameter to 10 after ...
		 */
		dico = new Dictionnaire(10); //
		this.url = url;

	}

	
	@Override
	public void run() {
		try {
			parseDocWikiStax(url);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void parseDocWikiStax(String url) throws InterruptedException {

		WikiXMLParser wxsp = WikiXMLParserFactory.getSAXParser(url);

		try {
			wxsp.setPageCallback(new PageCallbackHandler() {

				public void process(WikiPage page) {
					try {
						if (!page.isRedirect() && !page.isSpecialPage() && !page.isStub()) {
							final String pageWords[] = Utils.removePunctuation(page.getWikiText());
							String title = page.getTitle();
							updateIdPage(title);
							System.out.println(++nbPages);
							HashMap<String, Boolean> doublon = new HashMap<String, Boolean>();
							for (String word : pageWords) {
								if (Utils.recherche(word, dico.getHmapDico())) {
									if (assocMotPage.get(word) == null) {
										assocMotPage.put(word, new LinkedList<String>());
										doublon.put(word, true);
										assocMotPageRank.put(word, new LinkedList<Page>());
									}
									else if(doublon.get(word) == null){
										assocMotPage.get(word).add(title);
										doublon.put(word, true);
										assocMotPageRank.get(word).add(new Page(title));
									}
								}
							}
							Vector<String> tmpLinks = page.getLinks();
							for(String links : tmpLinks){
								links = links.toLowerCase();// casse minuscule
								updateIdPage(links);
								// mise à jour des tableaux C et I
								setCandIParameters(links, tmpLinks.size());
							}
							// mise à jour du tableau L contenant les lignes
							setLParameters();
						}
					}

					catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			wxsp.parse();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("[masterThread] pages are treating, awaiting the last thread ...");
		System.out.println("[masterThread] creating CLI matrix ...");
		matrice = new Matrice(C, L, I);
		System.out.println("[masterThread] CLI matrix created.");
		/**
		 * Ici commencer le produit
		 */
		System.out.println("[masterThread] creating Pk1 vector ...");
		vecteur = new Vecteur(Matrice.MAX_SIZE, (double) (1.0 / getNbPages()));
		System.out.println("[masterThread] Pk1 vector created.");
		System.out.println("[masterThread] computing pageRank ...");
		vecteur = Utils.calculatePageRank(matrice, vecteur);
		Utils.trier(assocMotPageRank);
		System.out.println("[masterThread] pageRank done.");
		Serialisation.save(vecteur, "cli.ser");
		System.out.println("[masterThread] pageRank saved.");
		//Vecteur.displayVector(vectResultat, getNbPages());
		try {
			System.out.println("Ecriture dans le fichier Log de la relation mot page");
			DiskIO.writeToFile(assocMotPage, "mot_page_relation.log");
			DiskIO.writeToFile(assocMotPageRank, "mot_page_relation_trie.log");
			System.out.println("Fin de l'écriture");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public synchronized boolean canNotify() {
		pageWorkerDone++;
		int i = 0;
		for (; i < 10; i++) {
			if (pageWorkerDone != getNumberOfPageWorker())
				break;
		}
		return i == 10 && pageWorkerDone == getNumberOfPageWorker();
	}

	public synchronized int getNumberOfPageWorker() {
		return numberOfPageWorker;
	}

	public synchronized void setNumberOfPageWorker(int numberOfPageWorker) {
		this.numberOfPageWorker = numberOfPageWorker;
	}

	public static Map<String, LinkedList<String>> getAssocMotPage() {
		// TODO Auto-generated method stub
		return assocMotPage;
	}

	public static Map<String, Integer> getIdPage() {
		// TODO Auto-generated method stub
		return idPage;
	}

	public synchronized void setCandIParameters(String link, int nbLink) {
		C[flag] = (double) (1.0 / (double) nbLink);
		I[flag++] = getIdPage().get(link);
	}

	public synchronized void setLParameters() {
		L[ind1] = flagDeb;
		L[ind1 + 1] = flag;
		ind1++;
		flagDeb = flag;
	}

	public synchronized int updateIdPage(String title) {
		int curPageId;
		boolean res = idPage.get(title) != null;
		if (!res) {
			curPageId = getId_page();
			idPage.put(title, curPageId);
		} else {
			curPageId = idPage.get(title);
		}
		return curPageId;
	}

	public Dictionnaire getDico() {
		return dico;
	}

	public synchronized int getNbPages() {
		return nbPages;
	}

	public synchronized void setNbPages(int nbPages) {
		this.nbPages = nbPages;
	}

	public double[] getC() {
		return C;
	}

	public int[] getL() {
		return L;
	}

	public int[] getI() {
		return I;
	}

	public synchronized int getId_page() {
		return id_page++;
	}
	
	public static Vecteur getVecteur(){
		return vecteur;
	}

}
