package maain.models;

import java.util.Vector;

import edu.jhu.nlp.wikipedia.PageCallbackHandler;
import edu.jhu.nlp.wikipedia.WikiPage;
import edu.jhu.nlp.wikipedia.WikiXMLParser;
import edu.jhu.nlp.wikipedia.WikiXMLParserFactory;
import maain.tp1.App;
import maain.tp1.MasterThread;

public class CLIWorker implements Runnable {

	// WikiPage page;
	MasterThread master;
	long id;

	public CLIWorker(MasterThread _master) {
		// page = _page;
		master = _master;
	}

	int nbPages = 0;

	@Override
	public void run() {

		WikiXMLParser wxsp = WikiXMLParserFactory.getSAXParser(App.path);
		try {
			wxsp.setPageCallback(new PageCallbackHandler() {

				public void process(final WikiPage page) {
					if (!page.isRedirect() && !page.isSpecialPage() && !page.isStub()) {
						Vector<String> tmpLinks = page.getLinks();
						// int curPageId =
						// master.updateIdPage(page.getTitle().toLowerCase());
						System.out.println(++nbPages);
						for (String links : tmpLinks) {
							links = links.toLowerCase();// casse minuscule
							master.updateIdPage(links);
							// mise à jour des tableaux C et I
							master.setCandIParameters(links, tmpLinks.size());
						}
						// mise à jour du tableau L contenant les lignes
						master.setLParameters();
					}
				}
			});
			wxsp.parse();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
