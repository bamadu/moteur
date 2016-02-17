package maain.tp1;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.xml.stream.XMLStreamException;

import org.jdom2.JDOMException;

import maain.utils.Utils;

public class App {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long startTime = System.nanoTime();
		//String deb = Utils.displayDate();
		 try {
			 String path = new File("").getAbsolutePath()+"/../../../m2-lp/maain-m2/frwiki.xml";
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
	     long elapsedTimeInMillis = TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.SECONDS);
	     System.out.println("Total elapsed time: " + elapsedTimeInMillis + " ms");
	}

}
