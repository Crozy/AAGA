package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class TestBedsGenerator {
	
	private static final int XBOUND = 1200;
	private static final int YBOUND = 800;
	
	
	public static ArrayList<Point> getNRandomPoints(int n) {
		 ArrayList<Point> list = new  ArrayList<>();
		Random rand = new Random();
		
		for (int i = 0; i < n; i++) {
			Point p = new Point(rand.nextInt(XBOUND), rand.nextInt(YBOUND));
			while(exists(list, p))
				p = new Point(rand.nextInt(XBOUND), rand.nextInt(YBOUND));
			
			list.add(p);
		}
		
		return list;
	}


	private static boolean exists(ArrayList<Point> list, Point p) {
		for(Point tmp: list)
			if(tmp.x == p.x && tmp.y == p.y)
				return true;
		return false;
	}
	
	public static void main(String []arg) {
		System.out.println(getNRandomPoints(10));
	}

}
