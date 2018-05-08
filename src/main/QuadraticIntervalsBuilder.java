package main;

import java.util.List;

import model.Interval;
import model.LinesInterval;
import model.Parabola;
import model.Point;
import model.QuadraticInterval;
import utils.Utils;

public class QuadraticIntervalsBuilder extends Utils {

	public static QuadraticIntervals build(LinearIntervals intervals, List<Point> points) {
		
		QuadraticIntervals res = new QuadraticIntervalsImpl(intervals.getInterval());
		int l = 0;
		int r = 1;
		print(points);
		List<LinesInterval> iis = intervals.intervals();
		int len = iis.size();
		for(int i=0; i<points.size()-1; ++i) {
			Point pl = points.get(i);
			Point pr = points.get(i+1);
			while (r < len && iis.get(r).getX2() < pr.getX()) ++r;
			while (l < len && iis.get(l).getX1() < pl.getX()) ++l;
			process(iis, pl, pr, l, r, res);
		}
		return res;
	}

	private static void process(List<LinesInterval> iis, Point pl, Point pr, int l, int r, QuadraticIntervals res) {
		print("l=" + l + " r=" + r);
		if (r - l != 1) fail("r - l != 1");
		LinesInterval li = iis.get(l);
		LinesInterval ri = iis.get(r);
		print("pl = " + pl + " pr = " + pr);
		Parabola pnA = new Parabola(0.5*li.getLower().getK(), li.getLower().getM(), 0);
		pnA = pnA.up(-pnA.at(pl.getX()) + pl.getY());
		
		Parabola pvA = new Parabola(0.5*li.getUpper().getK(), li.getUpper().getM(), 0);
		pvA = pvA.up(-pvA.at(pl.getX()) + pl.getY());
		
		Parabola pvB = new Parabola(0.5*ri.getLower().getK(), ri.getLower().getM(), 0);
		pvB = pvB.up(-pvB.at(pr.getX()) + pr.getY());
		
		Parabola pnB = new Parabola(0.5*ri.getUpper().getK(), ri.getUpper().getM(), 0);
		pnB = pnB.up(-pnB.at(pr.getX()) + pr.getY());
		
		print("X=" + pvA.at(pl.getX()));
		print("K=" + pvA);
		
		Interval pp = i(pl.getX(), pr.getX());
		
		List<Point> lowerC = pnA.cross(pnB);
		List<Point> upperC = pvA.cross(pvB);
		
		if (lowerC.size() == 0) {
			double x = pp.middle();
			lowerC = list(new Point(x, pnA.at(x)));
		}
		
		if (upperC.size() == 0) {
			double x = pp.middle();
			upperC = list(new Point(x, pvA.at(x)));
		}
		
		Point cn = lowerC.get(0);
		Point cv = upperC.get(0);

		if (!pp.contains(cn.getX())) {
			cn = lowerC.get(1);
		}
		if (!pp.contains(cv.getX())) {
			cv = upperC.get(1);
		}
		res.getLower().add(new QuadraticInterval(i(pl.getX(), cn.getX()), pnA));
		res.getLower().add(new QuadraticInterval(i(cn.getX(), pr.getX()), pnB));
		res.getUpper().add(new QuadraticInterval(i(pl.getX(), cv.getX()), pvA));
		res.getUpper().add(new QuadraticInterval(i(cv.getX(), pr.getX()), pvB));
	}

	private static Interval i(double l, double r) {
		return new Interval(l, r);
	}

}
