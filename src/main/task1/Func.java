package main.task1;

import java.util.List;
import java.util.function.Function;

import main.Intervals;
import main.Line;
import main.LineInterval;
import main.MyException;
import main.Point;

public class Func {

	public static Intervals func(
			List<Double> sp,
			List<Double> spozn,
			Function<Double, Double> y,
			Function<Double, Double> yd) {
		
		if (sp.size() != spozn.size() + 1) {
			throw new MyException("sp.size() != spozn.size() + 1");
		}
		Intervals res = new Intervals();
		for(int i=0; i<spozn.size(); ++i) {
			double x1 = sp.get(i);
			double x2 = sp.get(i+1);
			Line line1 = new Line(yd.apply(x1), y.apply(x1) - yd.apply(x1) * x1);
			Line line2 = new Line(yd.apply(x2), y.apply(x2) - yd.apply(x2) * x2);
			Point pA = new Point(x1, y.apply(x1));
			Point pB = new Point(x2, y.apply(x2));
			Point pC = line1.intersect(line2);
			double xm = pC.getX();
			Point pD = new Point(xm, y.apply(pC.getX()));
			Line lineAC = Line.make(pA, pC);
			Line lineAD = Line.make(pA, pD);
			Line lineBC = Line.make(pB, pC);
			Line lineBD = Line.make(pB, pD);
			double ozn = spozn.get(i);
			if (ozn < 0) {
				res.add(new LineInterval(x1, lineAC, lineAD));
				res.add(new LineInterval(xm, lineBC, lineBD));
			}
			if (ozn > 0) {
				res.add(new LineInterval(x1, lineAD, lineAC));
				res.add(new LineInterval(xm, lineBD, lineBC));
			}
		}
		res.add(new LineInterval(last(sp), new Line(0, 0), new Line(0, 0)));
		return res;
	}

	private static double last(List<Double> sp) {
		return sp.get(sp.size()-1);
	}
	
}
