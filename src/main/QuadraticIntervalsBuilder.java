package main;

import java.util.List;
import java.util.function.Function;

import model.Interval;
import model.LinesInterval;
import model.Parabola;
import model.Point;
import model.QuadraticInterval;
import utils.Utils;

public class QuadraticIntervalsBuilder extends Utils {

	public static QuadraticIntervals build(LinearIntervals intervals, Function<Double, Double> y) {
		QuadraticIntervals res = new QuadraticIntervalsImpl(intervals.getInterval());
		List<LinesInterval> iis = intervals.normalize().intervals();
		iis.forEach(li -> {
			process(li, res, y);
		});
		return res;
	}

	private static void process(LinesInterval li, QuadraticIntervals res, Function<Double, Double> y) {
		
		Point pl = new Point(li.getX1(), y.apply(li.getX1()));
		Point pr = new Point(li.getX2(), y.apply(li.getX2()));
		
		Parabola pnA = new Parabola(0.5*li.getLower().getK(), li.getLower().getM(), 0);
		pnA = pnA.up(-pnA.at(pl.getX()) + pl.getY());
		
		Parabola pvA = new Parabola(0.5*li.getUpper().getK(), li.getUpper().getM(), 0);
		pvA = pvA.up(-pvA.at(pl.getX()) + pl.getY());
		
		Parabola pvB = new Parabola(0.5*li.getLower().getK(), li.getLower().getM(), 0);
		pvB = pvB.up(-pvB.at(pr.getX()) + pr.getY());
		
		Parabola pnB = new Parabola(0.5*li.getUpper().getK(), li.getUpper().getM(), 0);
		pnB = pnB.up(-pnB.at(pr.getX()) + pr.getY());
		
		Interval pp = i(pl.getX(), pr.getX());
		
		List<Point> lowerC = pnA.cross(pnB);
		List<Point> upperC = pvA.cross(pvB);
		
		if (lowerC.size() == 0) 
		{
			fail("should not fail");
			double x = li.getX2();
			lowerC = list(new Point(x, pnA.at(x)));
		}
		
		if (upperC.size() == 0) 
		{
			fail("should not fail");
			double x = li.getX2();
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
