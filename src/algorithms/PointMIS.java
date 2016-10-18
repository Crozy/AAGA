package algorithms;

import java.awt.Point;

public class PointMIS {
	
	private Point p;
	private int idComposant;
	private boolean actif;
	private Couleur couleur;
	
	public PointMIS(Point p, int idComposant, boolean actif, Couleur couleur) {
		this.p = p;
		this.idComposant = idComposant;
		this.actif = actif;
		this.couleur = couleur;
	}
	
	public Point getP() {
		return p;
	}
	
	public void setP(Point p) {
		this.p = p;
	}
	
	public int getIdComposant() {
		return idComposant;
	}
	
	public Couleur getCouleur() {
		return couleur;
	}

	public void setCouleur(Couleur couleur) {
		this.couleur = couleur;
	}

	public void setIdComposant(int idComposant) {
		this.idComposant = idComposant;
	}
	
	public boolean isActif() {
		return actif;
	}
	
	public void setActif(boolean actif) {
		this.actif = actif;
	}
	
	public enum Couleur {
		  BLANC,
		  GRIS,
		  NOIR,
		  BLEU;	
	}
}
