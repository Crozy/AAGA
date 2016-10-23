package algorithms;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/* PR IS is defined as the ratio of the size of
 the constructed CDS over the size of MCDS. In this
 paper, we propose a new greedy algorithm with PR of
 4:8 þ ln 5, which is better than the current best one */

public class DefaultTeam {
	static HashMap<Point, ArrayList<Point>> voisins;
	public static int i = 53;

	public ArrayList<Point> calculConnectedDominatingSet(
			ArrayList<Point> points, int edgeThreshold)
			throws FileNotFoundException {
		
		/*try {
			File f = new File("input/input.points" + (++i));
			PrintStream fr = new PrintStream(new FileOutputStream(f));
			File f2 = new File("input.points");
			BufferedReader fr2 = new BufferedReader(new FileReader(f2));
			String ligne = "";
			while ((ligne = fr2.readLine()) != null) {
				fr.append(ligne+"\n");
			}
			fr.close();
			fr2.close();

		} catch (IOException e) {
			e.printStackTrace();
		}*/

		initVoisins(points, edgeThreshold);
		ArrayList<Point> list = new ArrayList<>();
		ArrayList<PointMIS> result = new ArrayList<>();
		// ArrayList<Point> copyPoints = pointsWithoutDoublon(points);
		// ArrayList<PointMIS> bestList = new ArrayList<PointMIS>();
		// int bestSize = Integer.MAX_VALUE;
//
//		for (int i = 0; i < copypoints.size(); i++) {
//			ArrayList<PointMIS> listMIS = SMIS.algoGlouton(copyPoints,
//					edgeThreshold, i);
//
//			if (bestsize > listMIS.size()) {
//				bestlist = listMIS;
//				bestsize = listMIS.size();
//			}
//		}
		
		try {
			File f = new File("output/resultSTEINER.points");
			PrintStream fr = new PrintStream(new FileOutputStream(f));
			ArrayList<Point> res;
//			ArrayList<PointMIS> resMIS;
			int[] nb = {500,1000,2000,5000,10000,50000};
			for(int i : nb){
				res = Util.getPointsFromFile("output/input"+i+".points");
				ArrayList<Point> copypoints = pointsWithoutDoublon(res);
				long start = System.nanoTime();
				list = DefaultTeamTME2.calculDominatingSet(copypoints, edgeThreshold);
				list = (new DefaultTeamSteiner()).calculSteinerPoints(copypoints, edgeThreshold, list);
//				 resMIS = SMIS.mis(res, edgeThreshold);
//				resMIS = SMIS.algoGlouton(res, edgeThreshold, 0);
				long end = (System.nanoTime() - start) / 1000000;
//				res = SMIS.pointsFromPointsMIS(resMIS);
				System.out.println("Did "+i);
				fr.append(i + " " + res.size() + " " + end +  "\n");
			}
			fr.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		do{
//		
//			int i = 10000;
//			RandomPointsGenerator.generate(i);
//			list = Util.getPointsFromFile("output/input"+i+".points");
//			initVoisins(list, edgeThreshold);
//			System.out.println("Yo");
//		}while(!Util.isConnexe(voisins, list));
//		
		result = SMIS.mis(points, edgeThreshold);
		list = SMIS.pointsFromPointsMIS(result);
		System.out.println("Size:" + list.size());
		return list;
	}

	public ArrayList<Point> clean(ArrayList<Point> domSet,
			ArrayList<Point> points, int edgeThreshold) {
		ArrayList<Point> copySteiner = new ArrayList<Point>(domSet);
		for (Point p : domSet) {
			copySteiner.remove(p);
			if (!Util.isValidAndConnexe(voisins, points, domSet, edgeThreshold))
				copySteiner.add(p);
		}
		return copySteiner;

	}

	public ArrayList<Point> optiLocal(ArrayList<Point> domSet,
			ArrayList<Point> points, int edgeThreshold) {
		ArrayList<Point> copySteiner = new ArrayList<Point>(domSet);
		for (Point p : domSet) {
			copySteiner.remove(p);
			for (Point p2 : domSet) {
				copySteiner.remove(p2);
				for (Point p3 : points) {
					if (copySteiner.contains(p3))
						continue;
					copySteiner.add(p3);
					if (Util.isValidAndConnexe(voisins, points, copySteiner,
							edgeThreshold)) {
						return copySteiner;
					} else
						copySteiner.remove(p3);
				}
				copySteiner.add(p2);
			}
			copySteiner.add(p);
		}
		return copySteiner;

	}

	public ArrayList<Point> enleverFeuilles(ArrayList<Point> steiner) {
		ArrayList<Point> todel = new ArrayList<Point>();
		for (Point p : steiner) {
			if (voisins.get(p).size() == 1)
				todel.add(p);

		}
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
			voisins.put(p, Util.neighbor(p, points, thresh));
		}
	}

}
