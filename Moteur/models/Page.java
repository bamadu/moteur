package maain.models;

import maain.tp1.MasterThread;



public class Page implements Comparable<Page>{
	
	private String titre;
	public Page(String t){
		titre = t;
	}
	
	
	@Override
	public int compareTo(Page o) {
		// TODO Auto-generated method stub
		double p1 = MasterThread.getVecteur().getTab()[MasterThread.getIdPage().get(this.titre)];
		double p2 = MasterThread.getVecteur().getTab()[MasterThread.getIdPage().get(o.titre)];
		if(p1 < p2) return -1;
		if(p1 > p2) return 1;
		return 0;
	}

	public String toString(){
		double p1 = MasterThread.getVecteur().getTab()[MasterThread.getIdPage().get(this.titre)];
		return titre + "<"+p1+"> ";
	}
	

}
