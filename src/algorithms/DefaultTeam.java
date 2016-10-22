package algorithms;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/* PR IS is defined as the ratio of the size of
 the constructed CDS over the size of MCDS. In this
 paper, we propose a new greedy algorithm with PR of
 4:8 þ ln 5, which is better than the current best one */

public class DefaultTeam {
	static HashMap<Point, ArrayList<Point>> voisins;
	public ArrayList<Point> calculConnectedDominatingSet(
			ArrayList<Point> points, int edgeThreshold)
			throws FileNotFoundException {
		for(int i = 0 ; i< 100 ; i++){
			File f = new File("input/input.points"+i);
			FileOutputStream fo = new FileOutputStream(f);
			try {
				fo.write(10);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		initVoisins(points, edgeThreshold);
		ArrayList<Point> list = null;
		ArrayList<Point> copypoints = pointsWithoutDoublon(points);
		ArrayList<PointMIS> bestlist = null;
		int bestsize = Integer.MAX_VALUE;
		
		for (int i = 0; i < copypoints.size(); i++) {
				ArrayList<PointMIS> listMIS = SMIS.algoGlouton(copypoints,
						edgeThreshold, i);

				if (bestsize > listMIS.size()) {
					bestlist = listMIS;
					bestsize = listMIS.size();
				}
		}
		
		list = SMIS.pointsFromPointsMIS(bestlist);
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
					}else
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
