package maain.tp1;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.xml.stream.XMLStreamException;

import org.jdom2.JDOMException;

import maain.utils.Utils;

public class App {
	
	private String path;
	
	public App() {
		path = getClass().getResource("test.xml").toString();
	}
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//App app = new App();
		long startTime = System.nanoTime();
		//String deb = Utils.displayDate();
		 try {
			 String path = "test.xml";
			Parse parse = new Parse(path);
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
		}
		
		//Parse parse = new Parse("/Users/Sacko/Documents/Master/Master_2/Semestre_2/MAIN/MesTPS/frwiki.xml");
		//System.out.println("deb : "+deb+", fin : "+Utils.displayDate());
		 long endTime = System.nanoTime();
	     long elapsedTimeInMillis = TimeUnit.SECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS);
	     System.out.println("Total elapsed time: " + elapsedTimeInMillis + " s");
	}

}
