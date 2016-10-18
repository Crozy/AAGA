package algorithms;

import java.awt.Point;

public class Couple {
	private int profondeur;
	private Point p;
	public Couple(Point p1,int r){
		profondeur=r;
		p=p1;
	}
	public int getProfondeur() {
		return profondeur;
	}
	public void setProfondeur(int profondeur) {
		this.profondeur = profondeur;
	}
	public Point getP() {
		return p;
	}
	public void setP(Point p) {
		this.p = p;
	}
	
}