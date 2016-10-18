package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import algorithms.PointMIS.Couleur;

public class SMIS {
	static HashMap<PointMIS, ArrayList<PointMIS>> voisins;

	public static void init(ArrayList<PointMIS> list, int edge) {
		voisins = new HashMap<>();
		for (PointMIS p : list) {
			voisins.put(p, neighbor(p, list, edge));
		}
	}

	public static ArrayList<PointMIS> mis(ArrayList<Point> points, int edge) {

		ArrayList<PointMIS> pointsMIS = new ArrayList<PointMIS>();
		ArrayList<PointMIS> noeudsBlanc = new ArrayList<PointMIS>();
		ArrayList<PointMIS> dominators = new ArrayList<PointMIS>();
		Random r = new Random();
		PointMIS mis;
		int i = 1;

		for (Point pm : points) {
			PointMIS p1 = new PointMIS(pm, i, false, Couleur.BLANC);
			pointsMIS.add(p1);
			noeudsBlanc.add(p1);
			i++;
		}
		init(pointsMIS, edge);

		mis = pointsMIS.get(r.nextInt(pointsMIS.size()));
		mis.setCouleur(Couleur.NOIR);
		dominators.add(mis);
		noeudsBlanc.remove(mis);

		for (PointMIS point1 : voisins.get(mis)) {
			point1.setCouleur(Couleur.GRIS);
			noeudsBlanc.remove(point1);
			for (PointMIS point2 : voisins.get(point1)) {
				point2.setActif(true);
			}
		}

		while (!noeudsBlanc.isEmpty()) {
			PointMIS bestActif = bestNoeudBlancActif(noeudsBlanc); // On prend le meilleur candidat 
			bestActif.setCouleur(Couleur.NOIR);
			dominators.add(bestActif); 	// On l'ajoute dans la liste des dominants 
			noeudsBlanc.remove(bestActif);
			for (PointMIS point1 : voisins.get(bestActif)) { // Pour chaque voisins du candidat on les met en gris
				point1.setCouleur(Couleur.GRIS);
				noeudsBlanc.remove(point1);
				for (PointMIS point2 : voisins.get(point1)) {
					point2.setActif(true);
				}
			}
		}

		return dominators;
	}
	public static ArrayList<Point> pointsFromPointsMIS(ArrayList<PointMIS> list){
		ArrayList<Point> list2 = new ArrayList<>();
		for(PointMIS p : list){
			list2.add(p.getP());
		}
			return list2;
	}
	public static PointMIS bestNoeudBlancActif(ArrayList<PointMIS> list) {
		PointMIS resMax = null;
		int nbMax = Integer.MIN_VALUE;
		for (PointMIS p1 : list) {
			if (p1.isActif() && p1.getCouleur() == Couleur.BLANC) {
				int nbVoisinsBlanc = nbVoisinsBlanc(p1);
				if (nbVoisinsBlanc > nbMax) {
					nbMax = nbVoisinsBlanc;
					resMax = p1;
				}
			}
		}
		return resMax;
	}

	public static int nbVoisinsBlanc(PointMIS p) {
		int nbVoisins = 0;
		for (PointMIS p2 : voisins.get(p)) {
			if (p2.getCouleur() == Couleur.BLANC)
				nbVoisins++;
		}
		return nbVoisins;
	}

	public static ArrayList<PointMIS> neighbor(PointMIS p, ArrayList<PointMIS> vertices, int edgeThreshold) {
		ArrayList<PointMIS> result = new ArrayList<PointMIS>();

		for (PointMIS point : vertices)
			if (point.getP().distance(p.getP()) <= edgeThreshold && !point.equals(p))
				result.add(point);

		return result;
	}

}
