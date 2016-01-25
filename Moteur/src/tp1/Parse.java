package tp1;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class Parse {
	private Page page;
	
	public Parse(String url){
		page = new Page();
		ParseDoc(url);
		
	}
	public void ParseDoc(String url){
		
		try {
			
			File input = new File(url);
			SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build(input);
			Element rootElement = doc.getRootElement();
			
			List<Element> libraryList= rootElement.getChildren();
			LinkedList<Page> pageList = new LinkedList();
			
			
			int j = 0;
			for(int i=0; i < libraryList.size(); i++ ){
				
				Element libraryChild = libraryList.get(i);
				page.setTitre(libraryChild.getChildText("titre"));
				page.setContenu(libraryChild.getChildText("contenu"));
				page.setId(j);
				pageList.add(page);
				j++;
			}
			
			System.out.println(pageList.size());
			
			for(int i=0; i < pageList.size(); i++){
				Page pagePrint = pageList.get(i);
				pagePrint.printId();
				pagePrint.printTitre();
				pagePrint.printContenu();
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
	
	public static void main(String args[]){
		Parse parse = new Parse("pagetest.xml");
	}
}
