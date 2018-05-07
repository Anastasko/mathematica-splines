package main;

import java.util.List;
import java.util.function.Function;

import model.Interval;
import model.Line;
import model.LineInterval;
import model.Point;
import utils.Utils;
import validation.MyException;

public class LinearIntervalsBuilder extends Utils {

	public static LinearIntervals build(
			List<Double> sp,
			List<Double> spozn,
			Function<Double, Double> y,
			Function<Double, Double> yd) {
		
		if (sp.size() != spozn.size() + 1) {
			throw new MyException("sp.size() != spozn.size() + 1");
		}
		for(int i=0; i < spozn.size(); ++i) {
			if (equals(sp.get(i), sp.get(i+1))) {
				throw new MyException("sp[" + i + "] == sp[" + (i+1) + "]\n" + sp);
			}
		}
		LinearIntervalsImpl res = new LinearIntervalsImpl(new Interval(first(sp), last(sp)));
		for(int i=0; i<spozn.size(); ++i) {
			double x1 = sp.get(i);
			double x2 = sp.get(i+1);
			Line line1 = new Line(yd.apply(x1), y.apply(x1) - yd.apply(x1) * x1);
			Line line2 = new Line(yd.apply(x2), y.apply(x2) - yd.apply(x2) * x2);
			Point pA = new Point(x1, y.apply(x1));
			Point pB = new Point(x2, y.apply(x2));
			Point pC = line1.equals(line2)
					? new Point((x1+x2)/2, y.apply((x1+x2)/2))
					: line1.intersect(line2);
			double xm = pC.getX();
			Point pD = new Point(xm, y.apply(pC.getX()));
			if (equals(pA.getX(), pC.getX())) {
				fail("pa.x == pc.x",
						"pA.x=" + pA.getX(),
						"x1=" + x1 + " x2=" + x2);
			}
			Line lineAC = Line.make(pA, pC);
			Line lineAD = Line.make(pA, pD);
			Line lineBC = Line.make(pB, pC);
			Line lineBD = Line.make(pB, pD);
			double ozn = spozn.get(i);
			if (ozn > 0) {
				res.addLower(new LineInterval(x1, xm, lineAC));
				res.addUpper(new LineInterval(x1, xm, lineAD));
				res.addLower(new LineInterval(xm, x2, lineBC));
				res.addUpper(new LineInterval(xm, x2, lineBD));
			}
			if (ozn < 0) {
				res.addLower(new LineInterval(x1, xm, lineAD));
				res.addUpper(new LineInterval(x1, xm, lineAC));
				res.addLower(new LineInterval(xm, x2, lineBD));
				res.addUpper(new LineInterval(xm, x2, lineBC));
			}
		}
		return res;
	}

}
