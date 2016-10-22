package algorithms;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

public class DefaultTeamTME2 {
	HashMap<Point, Boolean> visitedNodes = new HashMap<Point, Boolean>();
	private static final double EPSILON = 0;
	static HashMap<Point, ArrayList<Point>> voisins;

	public static  ArrayList<Point> calculDominatingSet(ArrayList<Point> points,
			int edgeThreshold) {
		ArrayList<Point> copypoints = pointsWithoutDoublon(points);
		initVoisins(points, edgeThreshold);

		ArrayList<Point> resmin;
		ArrayList<Point> res = calculDominatingSetGlouton(copypoints,
				edgeThreshold);
		ArrayList<Point>resg= res; //= localOpt2(copypoints, res, edgeThreshold);
		resmin = resg;
		// int i = 0;
		do {
			resg = DefaultTeamTME2.optiWhile(points, res, edgeThreshold);
			// i++;
			if (resmin.size() > resg.size()) {
				resmin = resg;
			}
			System.out.println(resmin.size());
			Collections.shuffle(res);
		} while (resmin.size() > 91);


		System.out.println("fin");
		return resmin;
	}

	public static ArrayList<Point> optiWhile(ArrayList<Point> pts,
			ArrayList<Point> dom, int seuil) {
		dom = localOpt2(pts, dom, seuil);
		int newscore = dom.size(), oldscore = dom.size();
		do {
			dom = localOpt22(pts, dom, seuil);
			oldscore = newscore;
			newscore = dom.size();
		} while (newscore < oldscore);
		return dom;
	}

	public static ArrayList<Point> pointsWithoutDoublon(ArrayList<Point> pts) {
		HashSet<Point> set = new HashSet<Point>();
		set.addAll(pts);
		ArrayList<Point> res = new ArrayList<Point>(set);
		return res;
	}

	public static void initVoisins(ArrayList<Point> points, int thresh) {
		voisins = new HashMap<Point, ArrayList<Point>>();
		for (Point p : points) {
			if (voisins.get(p) != null)
				System.out.println("il ya un doublon ici");
			voisins.put(p, Util.neighbor(p, points, thresh));
		}
	}

	public ArrayList<Point> algoBienChaud(ArrayList<Point> points,
			int edgeThreshold) {
		// int k = 0;
		ArrayList<Point> copyPoint = new ArrayList<Point>(points);
		ArrayList<Point> res = new ArrayList<Point>();
		Random rand = new Random();
		ArrayList<Point> snd;
		ArrayList<Point> first;
		ArrayList<Point> sndres;
		ArrayList<Point> firstres;
		while (!copyPoint.isEmpty()) {
			Point randomPoint = copyPoint.get(rand.nextInt(copyPoint.size()));
			int r = -1;

			do {
				r++;
				first = rthNeighbours(randomPoint, r, points, edgeThreshold);
				snd = rthNeighbours(randomPoint, r + 2, points, edgeThreshold);
				firstres = calculDominatingSetGlouton(first, edgeThreshold);
				sndres = calculDominatingSetGlouton(snd, edgeThreshold);

			} while (sndres.size() > (firstres.size() * (1 + EPSILON)));
			sndres = localOpt2(snd, sndres, edgeThreshold);
			res.addAll(sndres);
			Set<Point> hs = new HashSet<>();
			hs.addAll(res);
			res.clear();
			res.addAll(hs);
			copyPoint.removeAll(snd);
			// k++;
		}
		return res;
	}

	public ArrayList<Point> rthNeighbours(Point v, int r,
			ArrayList<Point> points, int edgeThreshold) {
		ArrayList<Point> result = new ArrayList<Point>();
		LinkedList<Couple> fifo = new LinkedList<Couple>();

		fifo.add(new Couple(v, r));
		while (!fifo.isEmpty()) {
			Couple c = fifo.pop();
			Point p = c.getP();
			int profondeur = c.getProfondeur();
			visitedNodes.put(p, true);
			if (visitedNodes.get(p) != null) { // Ajout dans la solution et dans
												// les visite
				result.add(p);
			}

			if (profondeur != 0) { // Fin de la recursion
				for (Point neighbour : voisins.get(p)) { // Ajout des fils a
															// visite
					if (visitedNodes.get(neighbour) == null) { // todelete
						fifo.addLast(new Couple(neighbour, profondeur - 1));
						visitedNodes.put(neighbour, true);
					}
				}
			}
		}
		visitedNodes.clear();
		return result;
	}

	public  static ArrayList<Point> calculDominatingSetGlouton(ArrayList<Point> points,
			int edgeThreshold) {
		ArrayList<Point> result = new ArrayList<Point>();
		ArrayList<Point> copyPoint = new ArrayList<Point>(points);
		while (copyPoint.size() != 0) {
			ArrayList<Point> maxAndNeighbours = Util.getMaxDegreePoint(
					copyPoint, edgeThreshold);

			result.add(maxAndNeighbours.get(0));
			copyPoint.removeAll(maxAndNeighbours);
		}
		return result;
	}

	public ArrayList<Point> randomOpts(ArrayList<Point> points,
			ArrayList<Point> dominatingSet, int seuil) {
		for (Point p : dominatingSet) {
			ArrayList<Point> domCopy = new ArrayList<Point>(dominatingSet);
			domCopy.remove(p);
			if (Util.isValidOpti(voisins, points, domCopy))
				return domCopy;
		}
		return dominatingSet;
	}

	// LocalOpt une passe
	public static ArrayList<Point> localOpt2(ArrayList<Point> points,
			ArrayList<Point> dominatingSet, int seuil) {
		// int i = 0;
		for (Point p1 : dominatingSet) {
			for (Point p2 : dominatingSet) {
				if (p1.equals(p2))
					continue;

				ArrayList<Point> domCopy = new ArrayList<Point>(dominatingSet);
				domCopy.remove(p1);
				domCopy.remove(p2);

				if (p1.distance(p2) > 2.5 * seuil) {
					continue;
				}
				ArrayList<Point> origCopy = new ArrayList<Point>(points);
				origCopy.removeAll(dominatingSet);
				for (Point p3 : origCopy) {
					if (p3.equals(p1) || p3.equals(p2))
						continue;
					if (p3.distance(p1) > 2.5 * seuil
							&& p3.distance(p2) > 2.5 * seuil) {
						continue;
					}
					domCopy.add(p3);
					if (Util.isValidOpti(voisins, points, domCopy)) {
						dominatingSet = domCopy;
						break;
					}
					domCopy.remove(p3);
				}
			}
		}
		return dominatingSet;
	}

	public static ArrayList<Point> localOpt22(ArrayList<Point> points,
			ArrayList<Point> dominatingSet, int seuil) {
		// int i = 0;
		for (Point p1 : dominatingSet) {
			for (Point p2 : dominatingSet) {
				if (p1.equals(p2))
					continue;

				ArrayList<Point> domCopy = new ArrayList<Point>(dominatingSet);
				domCopy.remove(p1);
				domCopy.remove(p2);

				if (p1.distance(p2) > 2.5 * seuil) {
					continue;
				}
				ArrayList<Point> origCopy = new ArrayList<Point>(points);
				origCopy.removeAll(dominatingSet);
				for (Point p3 : origCopy) {
					if (p3.equals(p1) || p3.equals(p2))
						continue;
					if (p3.distance(p1) > 2.5 * seuil
							&& p3.distance(p2) > 2.5 * seuil) {
						continue;
					}
					domCopy.add(p3);
					if (Util.isValidOpti(voisins, points, domCopy)) {
						return domCopy;
					}
					domCopy.remove(p3);
				}
			}
		}
		return dominatingSet;
	}

	// FILE PRINTER
	public void saveToFile(String filename, ArrayList<Point> result) {
		int index = 0;
		try {
			while (true) {
				BufferedReader input = new BufferedReader(
						new InputStreamReader(new FileInputStream(filename
								+ Integer.toString(index) + ".points")));
				try {
					input.close();
				} catch (IOException e) {
					System.err.println("I/O exception: unable to close "
							+ filename + Integer.toString(index) + ".points");
				}
				index++;
			}
		} catch (FileNotFoundException e) {
			printToFile(filename + Integer.toString(index) + ".points", result);
		}
	}

	private void printToFile(String filename, ArrayList<Point> points) {
		try {
			PrintStream output = new PrintStream(new FileOutputStream(filename));
			// int x,y;
			for (Point p : points)
				output.println(Integer.toString((int) p.getX()) + " "
						+ Integer.toString((int) p.getY()));
			output.close();
		} catch (FileNotFoundException e) {
			System.err.println("I/O exception: unable to create " + filename);
		}
	}

	// FILE LOADER
	public ArrayList<Point> readFromFile(String filename) {
		String line;
		String[] coordinates;
		ArrayList<Point> points = new ArrayList<Point>();
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(
					new FileInputStream(filename)));
			try {
				while ((line = input.readLine()) != null) {
					coordinates = line.split("\\s+");
					points.add(new Point(Integer.parseInt(coordinates[0]),
							Integer.parseInt(coordinates[1])));
				}
			} catch (IOException e) {
				System.err.println("Exception: interrupted I/O.");
			} finally {
				try {
					input.close();
				} catch (IOException e) {
					System.err.println("I/O exception: unable to close "
							+ filename);
				}
			}
		} catch (FileNotFoundException e) {
			System.err.println("Input file not found.");
		}
		return points;
	}
}