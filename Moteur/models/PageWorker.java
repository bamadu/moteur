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
						  
			 /* Prendre en compte la casse des titres */
			Vector<String> tmpLinks = page.getLinks();
			//int curPageId = master.updateIdPage(page.getTitle().toLowerCase());
			
			for(String links : tmpLinks){
				links = links.toLowerCase();// casse minuscule
				master.updateIdPage(links);
				master.setCandIParameters(links, tmpLinks.size());
			}

			master.setLParameters();
			/* association mot page (à revoir) */
			fillMotPageRelation(Utils.removePunctuation(page.getWikiText()), page.getTitle().toLowerCase(), master.getAssocMotPage());
			//System.out.println("END OF : "+ Thread.currentThread().getId()+" title Page: "+page.getTitle().toLowerCase());
			System.out.println(master.getNbPages());

		} 
		catch(java.lang.OutOfMemoryError er) {
			System.out.println("[pageWorker] "+Thread.currentThread().getId());
			er.printStackTrace();
		}
		if (master.canNotify()) {
			System.out.println("[pageWorker] last thread, Id "+Thread.currentThread().getId());
			notify();
			//System.out.println("[pageWorker] last thread, Id "+Thread.currentThread().getId());
		} else {
			System.out.println("[pageWorker] Id "+Thread.currentThread().getId());
		}
		//System.out.println("End   -> Worker "+Thread.currentThread().getId()+" currentTotal "+Thread.activeCount());
	}

	/**
	 * 
	 * @param page une page wikipedia
	 * @param map relation mot page
	 * 
	 */
	private void fillMotPageRelation(String pageWords[], String titre, Map<String, LinkedList<String>>  map) {
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
