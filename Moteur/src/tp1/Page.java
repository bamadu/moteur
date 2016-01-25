package tp1;

import java.util.ArrayList;

public class Page {
	
	private String titre;
	private String contenu;
	private int id;
	
	public Page(){
		
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
	
	public String printTitre(){
		return titre;
	}
	
	public String printContenu(){
		return contenu;
	}
	
	public int printId(){
		return id;
	}
}
