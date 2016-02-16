package tp1;

import java.util.LinkedList;


public class Page {
	
	private String titre="";
	private String contenu="";
	private int id;
	private LinkedList<String > links;
	
	public Page(){
		setLinks(new LinkedList<String>());
	}
	
	public void setTitre(String titre){
		this.titre = titre;
	}
	
	public void setContenu(String contenu){
		this.contenu = contenu;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public String getTitre(){
		return titre;
	}
	
	public String getContenu(){
		return contenu;
	}
	
	public int getId(){
		return id;
	}

	public LinkedList<String > getLinks() {
		return links;
	}

	public void setLinks(LinkedList<String > links) {
		this.links = links;
	}
}
