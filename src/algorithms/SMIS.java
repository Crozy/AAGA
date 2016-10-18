package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
		
		i = 1;
		for (PointMIS pm : dominators)
			pm.setIdComposant(i++);
		
		ArrayList<PointMIS> blue = CDS(pointsMIS,dominators);
		dominators.addAll(blue);
		System.out.println("Les bleus: " + blue.size());
		
		return dominators;
	}
	private static ArrayList<PointMIS> CDS(ArrayList<PointMIS> points, ArrayList<PointMIS> dominator) {
		ArrayList<PointMIS> blueNodes = new ArrayList<PointMIS>();
		for(int i=5;i>1;i--) {
			System.out.println("i: " + i);
			for(PointMIS p : greyNodesWithIBlackNeighbours(dominator, points, i)){
				p.setCouleur(Couleur.BLEU);
				blueNodes.add(p);
				int minVal = getMinIDComponent(p);
				p.setIdComposant(minVal);
				for(PointMIS neighbour : voisins.get(p)){
					neighbour.setIdComposant(minVal);
				}
			}
		}
		System.out.println("cds: " + blueNodes.size());
		return blueNodes;
	}
	public static int getMinIDComponent(PointMIS p){
		int minVal = Integer.MAX_VALUE;
		for(PointMIS p2 : voisins.get(p)){
			if(p2.getIdComposant() < minVal)
				minVal = p2.getIdComposant();
		}
		return minVal;
		
	}
	public static ArrayList<PointMIS> greyNodesWithIBlackNeighbours(ArrayList<PointMIS> black, ArrayList<PointMIS> list, int i){
		ArrayList<PointMIS> result = new ArrayList<PointMIS>();
		for(PointMIS p : list){
			if(p.getCouleur() != Couleur.GRIS)
				continue;
			//Calculates number of adjacent black neighbours from differents
			HashSet<Integer> idComponentSet = new HashSet<Integer>();
			
			for( PointMIS p2 : voisins.get(p)){
				if(p2.getCouleur() == Couleur.NOIR)
					idComponentSet.add(p2.getIdComposant());
			}

			//Look if the node satisfies i neighbours
			if(idComponentSet.size() == i)
				result.add(p);
			
		}
		return result;
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
