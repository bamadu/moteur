package maain.models;


import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;

import maain.tp1.MasterThread;
import maain.utils.Utils;
import edu.jhu.nlp.wikipedia.WikiPage;


public class PageWorker implements Runnable {

	WikiPage page;
	MasterThread master;

	public PageWorker(WikiPage page, MasterThread master) {
		this.page = page;
		this.master = master;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		try {
			//System.out.println("Start -> Worker "+Thread.currentThread().getId()+" currentTotal "+Thread.activeCount());
		/* $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ */
		//int ind1 = 0; //
		///int id_page = 0; //
		int curPageId = 0;
		//int flag = 0; // 
		//int flagDeb = 0; //
		//String tmpTitle;

		Vector<String> tmpLinks;

		//curPageId = master.getId_page();
		//tmpTitle = page.getTitle().toLowerCase();
		curPageId = master.updateIdPage(page.getTitle().toLowerCase());

		/**
		 * Prendre en compte la casse des titres
		 */
		tmpLinks = page.getLinks();
		synchronized (master) {
			for(String links : tmpLinks){
				links = links.toLowerCase();// casse minuscule
				master.updateIdPage(links);
				// C[flag] = (double) (1.0 / (double)tmpLinks.size()); 
				//I[flag++] = idPage.get(links); 
				//master.getC()[master.flag] = (double) (1.0 / (double)tmpLinks.size());
				//master.getI()[master.flag++] = master.getIdPage().get(links);
				master.setCandIParameters(links, tmpLinks.size());
			}

//			master.getL()[master.ind1] = master.flagDeb ;
//			master.getL()[master.ind1+1] = master.flag ;
//			master.ind1++;
//			master.flagDeb = master.flag ;
			master.setLParameters();
			/* association mot page (à revoir) */
			fillMotPageRelation(Utils.removePunctuation(page.getWikiText()), page.getTitle().toLowerCase(), master.getAssocMotPage());
			master.getIdLinks().put(curPageId, tmpLinks);
		}

		} catch(java.lang.OutOfMemoryError er) {
			System.out.println("Worker "+Thread.currentThread().getId());
			er.printStackTrace();
		}
		System.gc();
		//System.out.println("End   -> Worker "+Thread.currentThread().getId()+" currentTotal "+Thread.activeCount());
	}

	/**
	 * 
	 * @param page une page wikipedia
	 * @param map relation mot page
	 * 
	 */
	private void fillMotPageRelation(String pageWords[], String titre, Map<String, LinkedList<String>> map){
		//String pageWords[] = Clean.removePunctuation(page.getContenu());
	    /*
	     * Pour chaque mot de la page
	     * on verifie s'il existe dans le dictionnaire
	     * algo de recherche dichotomique 
	     */
	    for(String word : pageWords){
	    	if( Utils.recherche(word, master.getDico().getSortDataFinal())){
	    		if(map.get(word) == null){// 
	    			map.put(word, new LinkedList<String>());
	    		}
	    		else{
	    			map.get(word).add(titre);
	    		}
	    	}
	    }
	}


}
