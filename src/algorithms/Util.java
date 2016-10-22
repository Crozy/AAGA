package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public class Util {

	public static ArrayList<Point> neighbor(Point p, ArrayList<Point> vertices,
			int edgeThreshold) {
		ArrayList<Point> result = new ArrayList<Point>();

		for (Point point : vertices)
			if (point.distance(p) <= edgeThreshold && !point.equals(p))
				result.add((Point) point.clone());

		return result;
	}

	public static ArrayList<Point> getMaxDegreePoint(ArrayList<Point> list, int seuil) {
		Integer max = Integer.MIN_VALUE;
		Point pmax = null;
		ArrayList<Point> bestList=new ArrayList<Point>();
		ArrayList<Point> listOfNeighbours=new ArrayList<Point>();

		for (Point p1 : list) {
			int n = 0;
			listOfNeighbours.clear();
			for (Point p2 : list) {
				if (p1.equals(p2))
					continue;
				if (p1.distance(p2) < seuil) {
					n++;
					listOfNeighbours.add(p2);
				}
			}
			if (n > max) {
				pmax = p1;
				max = n;
				bestList=new ArrayList<Point>(listOfNeighbours);
			}
		}
		bestList.add(0, pmax);

		return bestList;
	}
	public static HashMap<Point,ArrayList<Point>> initVoisins(ArrayList<Point> points,int edgeThreshold){
		HashMap<Point,ArrayList<Point>> voisins = new HashMap<Point, ArrayList<Point>>();
			for (Point p : points) {
				if (voisins.get(p) != null){
					voisins.put(p, Util.neighbor(p, points, edgeThreshold));
				}else
					voisins.put(p,new ArrayList<Point>());
			}
			return voisins;
	}
	public static boolean isValidAndConnexe(HashMap<Point,ArrayList<Point>> voisins,
			ArrayList<Point> points,
			ArrayList<Point> dominatingSet,
			int edgeThreshold){
		boolean res=isValidOpti(voisins, points, dominatingSet);
		// 
		HashMap<Point,ArrayList<Point>> steinerNeighbours = Util.initVoisins(dominatingSet, edgeThreshold);
		HashMap<Point, Boolean> colored = new HashMap<Point, Boolean>();
		for (Point point : dominatingSet)
			colored.put(point, new Boolean(false));
		
		isConnexe(dominatingSet.get(0), steinerNeighbours, colored);
		
		for(Entry<Point,Boolean> p : colored.entrySet()){
			if(!p.getValue()) {
				return false;
			}
		}
		
		return res;
	}
	
	public static void isConnexe(Point p , HashMap<Point,ArrayList<Point>> voisins, HashMap<Point, Boolean> colored) {
		colored.put(p, new Boolean(true));
		
		
		for(Point point: voisins.get(p)) {
			if(!colored.get(point)) {
				isConnexe(point, voisins, colored);
			}
		}
		
	}
	
	public static boolean isValidOpti(HashMap<Point,ArrayList<Point>> voisins,
			ArrayList<Point> points,
			ArrayList<Point> dominatingSet){
		HashSet<Point> set=new HashSet<>();
		for(Point p : dominatingSet){
			ArrayList<Point> neighbours2=voisins.get(p);
			set.add(p);
			set.addAll(neighbours2);
			
		}
		return set.size()==points.size();
	}
}
