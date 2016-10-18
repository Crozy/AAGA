package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

/* PR IS is defined as the ratio of the size of
 the constructed CDS over the size of MCDS. In this
 paper, we propose a new greedy algorithm with PR of
 4:8 þ ln 5, which is better than the current best one */

public class DefaultTeam {
	static HashMap<Point, ArrayList<Point>> voisins;

	public ArrayList<Point> calculConnectedDominatingSet(ArrayList<Point> points, int edgeThreshold) {
		initVoisins(points, edgeThreshold);
		
		ArrayList<Point> copypoints = pointsWithoutDoublon(points);
		ArrayList<PointMIS> listMIS = SMIS.mis(copypoints, edgeThreshold);
		ArrayList<Point> list = SMIS.pointsFromPointsMIS(listMIS);
		
		return list;
	}
	public ArrayList<Point> clean(ArrayList<Point> domSet,ArrayList<Point> points, int edgeThreshold){
		ArrayList<Point> copySteiner= new ArrayList<Point>(domSet);
		for(Point p :  domSet){
			copySteiner.remove(p);
			if(!Util.isValidAndConnexe(voisins, points, domSet, edgeThreshold))
				copySteiner.add(p);
			else
				System.out.println("Mdr "+copySteiner.size());
		}
		System.out.println("COoy: " + copySteiner.size());
		return copySteiner;
		
	}
	public ArrayList<Point> enleverFeuilles(ArrayList<Point> steiner) {
		ArrayList<Point> todel=new ArrayList<Point>();
		for(Point p : steiner){
			if(voisins.get(p).size()==1)
				todel.add(p);
				
		}
		System.out.println("Best bet "+todel.size());
		steiner.removeAll(todel);
		
		return steiner;
	}
	
	public ArrayList<Point> pointsWithoutDoublon(ArrayList<Point> pts) {
		HashSet<Point> set = new HashSet<Point>();
		set.addAll(pts);
		ArrayList<Point> res = new ArrayList<Point>(set);
		return res;
	}

	public void initVoisins(ArrayList<Point> points, int thresh) {
		voisins = new HashMap<Point, ArrayList<Point>>();
		for (Point p : points) {
			if (voisins.get(p) != null)
				System.out.println("il ya un doublon ici");
			voisins.put(p, Util.neighbor(p, points, thresh));
		}
	}

	public ArrayList<Point> calculateMIS(ArrayList<Point> points) {
		Random r = new Random();
		ArrayList<Point> res = new ArrayList<Point>();
		ArrayList<Point> copyPoints = new ArrayList<Point>(points);

		while (!copyPoints.isEmpty()) {
			int randInt = r.nextInt(copyPoints.size());
			Point randPoint = copyPoints.get(randInt);
			res.add(randPoint);
			copyPoints.remove(randPoint);
			copyPoints.removeAll(voisins.get(randPoint));
		}
		return res;
	}

}
