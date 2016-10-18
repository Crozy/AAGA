package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.Callable;

public class MyCallable implements Callable<ArrayList<Point>> {

	ArrayList<Point> points;
	ArrayList<Point> resPoints;
	int edgeThreshold;

	public MyCallable(ArrayList<Point> points, ArrayList<Point> resPoints, int edgeThreshold) {
		this.points = points;
		this.resPoints = resPoints;
		this.edgeThreshold = edgeThreshold;
	}
	
	@Override
	public ArrayList<Point> call() throws Exception {
	
		ArrayList<Point> res, resmin;
		resmin = resPoints;
		int i = 0;
		Random r = new Random();
		
		do {
			r.setSeed(System.currentTimeMillis() );
			res = DefaultTeamTME2.optiWhile(points, resPoints, edgeThreshold);
			i++;
			if (resmin.size() > res.size()) {
				resmin = res;
			}
			Collections.shuffle(resPoints);
			System.out.println("i: " + i + " et " + Thread.currentThread().getName() +" taille : " + resmin.size());
		} while (resmin.size()>75 && i < 3);		
		System.out.println("Thread: " + Thread.currentThread().getName() + " best size: " + resmin.size());
		
		return resmin;
	}

}
