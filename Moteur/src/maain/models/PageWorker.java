package maain.models;

import java.util.HashMap;
import java.util.LinkedList;
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


		/* $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ */
		//int ind1 = 0; //
		///int id_page = 0; //
		int curPageId = 0;
		//int flag = 0; // 
		//int flagDeb = 0; //
		String tmpTitle;

		Vector<String> tmpLinks;

		//curPageId = master.getId_page();
		tmpTitle = page.getTitle().toLowerCase();
		curPageId = master.updateIdPage(tmpTitle);

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
				master.getC()[master.flag] = (double) (1.0 / (double)tmpLinks.size());
				master.getI()[master.flag++] = master.getIdPage().get(links);
			}

			master.getL()[master.ind1] = master.flagDeb ;
			master.getL()[master.ind1+1] = master.flag ;
			master.ind1++;
			master.flagDeb = master.flag ;
			/* association mot page (à revoir) */
			MasterThread.fillMotPageRelation(Utils.removePunctuation(page.getWikiText()), tmpTitle, master.getAssocMotPage());
			master.getIdLinks().put(curPageId, tmpLinks);
		}

	}



}
