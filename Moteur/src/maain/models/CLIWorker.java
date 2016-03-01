package maain.models;

import java.util.Vector;

import edu.jhu.nlp.wikipedia.WikiPage;
import maain.tp1.MasterThread;

public class CLIWorker implements Runnable {

	WikiPage page;
	MasterThread master;
	long id;
	public CLIWorker (WikiPage _page, MasterThread _master) {
		page = _page;
		master = _master;
		id = Thread.currentThread().getId();
	}

	@Override
	public void run() {
		try {
			//System.out.println("Start -> Worker "+Thread.currentThread().getId()+" currentTotal "+Thread.activeCount());
						  
			 /* Prendre en compte la casse des titres */
			Vector<String> tmpLinks = page.getLinks();
			//int curPageId = master.updateIdPage(page.getTitle().toLowerCase());
			
			for(String links : tmpLinks){
				links = links.toLowerCase();// casse minuscule
				master.updateIdPage(links);
				// mise à jour des tableaux C et I
				master.setCandIParameters(links, tmpLinks.size());
			}
			// mise à jour du tableau L contenant les lignes
			master.setLParameters();
			//System.out.println("END OF : "+ Thread.currentThread().getId()+" title Page: "+page.getTitle().toLowerCase());
			//System.out.println("["+Thread.currentThread().getId()+"] "+master.getNbPages());

		} 
		catch(java.lang.OutOfMemoryError er) {
			System.out.println("Worker "+Thread.currentThread().getId());
			er.printStackTrace();
		}
	}

}
