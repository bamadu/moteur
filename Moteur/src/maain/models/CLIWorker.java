package maain.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import edu.jhu.nlp.wikipedia.PageCallbackHandler;
import edu.jhu.nlp.wikipedia.WikiPage;
import edu.jhu.nlp.wikipedia.WikiXMLParser;
import edu.jhu.nlp.wikipedia.WikiXMLParserFactory;
import maain.diskIO.Serialisation;
import maain.utils.Utils;

public class CLIWorker implements Runnable {

	private String path; 
	private static Map<String, Integer> idPage;
	private static Vecteur vecteur;
	private Matrice matrice;
	private int id_page = 0;
	private static double C[];
	private static int L[];
	private static int I[];
	private static int nbPages = 0;
	private static int nbTreatPages = 0;
	
	public int flag = 0; //
	public int flagDeb = 0;
	public int ind1 = 0;
	
	
	public CLIWorker (String url) {
		path = url;
		idPage = new HashMap<String, Integer>();
		C = new double[Matrice.MAX_SIZE];
		L = new int[Matrice.MAX_SIZE];
		I = new int[Matrice.MAX_SIZE];
	}
	
	public static void main(String[] args) {
		Thread t = new Thread(new CLIWorker("/Users/seydou/m2-lp/maain-m2/frwiki.xml"));
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Nombre de pages total  : "+nbPages);
		System.out.println("Nombre de pages Traités: "+nbTreatPages);
	}

	@Override
	public void run() {
		
		WikiXMLParser wxsp = WikiXMLParserFactory.getSAXParser(path);
		try {
			wxsp.setPageCallback(new PageCallbackHandler() {
				public void process(WikiPage page) {
					nbPages++;
					if (!page.isRedirect() && !page.isSpecialPage() && !page.isStub()) {
						nbTreatPages++;
						/* Prendre en compte la casse des titres */
						Vector<String> tmpLinks = page.getLinks();
						//int curPageId = master.updateIdPage(page.getTitle().toLowerCase());
						
						for(String links : tmpLinks){
							links = links.toLowerCase();// casse minuscule
							updateIdPage(links);
							// mise à jour des tableaux C et I
							setCandIParameters(links, tmpLinks.size());
						}
						// mise à jour du tableau L contenant les lignes
						setLParameters();
						//System.out.println("END OF : "+ Thread.currentThread().getId()+" title Page: "+page.getTitle().toLowerCase());
						//System.out.println("["+Thread.currentThread().getId()+"] "+master.getNbPages());
					}
					System.out.println(nbTreatPages);
				}
			});
			wxsp.parse();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		catch(java.lang.OutOfMemoryError er) {
			System.out.println("Worker "+Thread.currentThread().getId());
			er.printStackTrace();
		}
		
		System.out.println("[MotPageWorker] pages are treating, awaiting the last thread ...");
		System.out.println("[MotPageWorker] creating CLI matrix ...");
		matrice = new Matrice(C, L, I);
		System.out.println("[MotPageWorker] CLI matrix created.");
		Serialisation.save(matrice);
		System.out.println("[MotPageWorker] CLI matrix saved.");
		/**
		 * Ici commencer le produit
		 */
		System.out.println("[MotPageWorker] creating Pk1 vector ...");
		vecteur = new Vecteur(Matrice.MAX_SIZE, (double) (1.0 / nbTreatPages)); 
		System.out.println("[MotPageWorker] Pk1 vector created.");
		//wait();
		System.out.println("[MotPageWorker] computing pageRank ...");
		Vecteur vectResultat = Utils.calculatePageRank(matrice, vecteur);
		System.out.println("[MotPageWorker] pageRank done.");
		Serialisation.save(vectResultat);
		System.out.println("[MotPageWorker] pageRank saved.");
		Vecteur.displayVector(vectResultat, nbTreatPages);
	}

	
	public int updateIdPage(String title) {
		int curPageId;
		boolean res = idPage.values().contains(title);
		if (!res) {
			curPageId = getId_page();
			idPage.put(title, curPageId);
		} else {
			curPageId = idPage.get(title);
		}
		return curPageId;
	}
	
	public int getId_page() {
		return id_page++;
	}
	
	public void setCandIParameters(String link, int nbLink) {
		C[flag] = (double) (1.0 / (double) nbLink);
		I[flag++] = getIdPage().get(link);
	}

	public void setLParameters() {
		L[ind1] = flagDeb;
		L[ind1 + 1] = flag;
		ind1++;
		flagDeb = flag;
	}
	
	public Map<String, Integer> getIdPage() {
		// TODO Auto-generated method stub
		return idPage;
	}
	
}
