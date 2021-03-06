package algorithms;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
		
//		isConnexe(dominatingSet.get(0), steinerNeighbours, colored);
		
		for(Entry<Point,Boolean> p : colored.entrySet()){
			if(!p.getValue()) {
				return false;
			}
		}
		
		return res;
	}
	
	public static boolean isConnexe(HashMap<Point,ArrayList<Point>> voisins, ArrayList<Point> list) {
		HashMap<Point, Boolean> bool = new HashMap<>();
		for(Point point: list) 
			bool.put(point, false);
		
		for(Point point: list) {
			for(Point neighbours : voisins.get(point)){
				if(!bool.get(neighbours))
					bool.put(neighbours, true);
			}
		}
		
		for(Point point: list) {
			if(!bool.get(point))
				return false;
		}
		return true;
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
	
	public static ArrayList<Point> getPointsFromFile(String filename) {
		ArrayList<Point> points = new ArrayList<>();
		
		try {
			File f = new File(filename);
			BufferedReader fr = new BufferedReader(new FileReader(f));
			String ligne = "";
			while ((ligne = fr.readLine()) != null) {
				String[] tmp = ligne.split(" ");
				points.add(new Point(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1])));
			}
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return points;
	}
	
}
