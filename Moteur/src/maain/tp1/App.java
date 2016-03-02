package maain.tp1;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.xml.stream.XMLStreamException;

import org.jdom2.JDOMException;


public class App {
	
	//public static final String path = "test.xml";
	//public static final String path = "/Users/seydou/m2-lp/maain-m2/frwiki.xml";
	public static final String path = "/Users/Sacko/Documents/Master/Master_2/Semestre_2/MAIN/MesTPS/frwiki.xml";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//App app = new App();
		long startTime = System.nanoTime();
		//String deb = Utils.displayDate();
		 try {
			 
			 MasterThread parse = new MasterThread (path);
			 /*Thread tCliWorker = new Thread(new CLIWorker(parse));
			 tCliWorker.start();
			 tCliWorker.join();*/
			 
			 Thread moulinette = new Thread(parse);
			 moulinette.start();
			 moulinette.join();
			//MasterThread parse = new MasterThread(path, 4);
			
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			System.out.println("From XML");
			e.printStackTrace();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			System.out.println("Jdom problem");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Entrée/Sortie");
			e.printStackTrace();
		} catch (java.lang.OutOfMemoryError e) {
			// TODO: handle exception
			System.out.println("Out Of Memory, NB active Worker "+Thread.activeCount());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Parse parse = new Parse("/Users/Sacko/Documents/Master/Master_2/Semestre_2/MAIN/MesTPS/frwiki.xml");
		//System.out.println("deb : "+deb+", fin : "+Utils.displayDate());
		 long endTime = System.nanoTime();
	     long elapsedTimeInMillis = TimeUnit.SECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS);
	     System.out.println("Total elapsed time: " + elapsedTimeInMillis + " s");
	}

}
