package validation;

import java.util.List;

import main.LinearIntervals;
import model.Line;
import model.LineInterval;
import utils.Utils;

public class Validator extends Utils {
	
	public static void validate(List<LineInterval> list) {
		for(int i=1; i<list.size(); ++i) {
			double p = list.get(i).getX1();
			double pp = list.get(i-1).getX1();
			if (less(p, pp)) {
				fail(p + " < " + pp);
			}
		}
		for(LineInterval li : list) {
			validate(li);
		}
	}
	
	public static void validate(LineInterval li) {
		if (Double.isNaN(li.getX1())) fail("x1 is NaN");
		if (Double.isNaN(li.getX2())) fail("x2 is NaN");
		validate(li.getLine());
		if (less(li.getX2(), li.getX1())) fail("x2 < x1");
	}

	public static void validate(Line l) {
		if (Double.isNaN(l.getK())) fail("k is NaN");
		if (Double.isNaN(l.getM())) fail("m is NaN");
	}

	public static void validate(LinearIntervals i) {
		validate(i.getLower());
		validate(i.getUpper());
//		for(LinesInterval in : i.intervals()) {
//			if (greater(in.getLowerAt(in.getX1()), in.getUpperAt(in.getX1()))) {
//				return fail(in, "lower > upper at x1", in.getLowerAt(in.getX1()) + " > " + in.getUpperAt(in.getX1()));
//			}
//			if (greater(in.getLowerAt(in.getX2()), in.getUpperAt(in.getX2()))) {
//				return fail(in, "lower > upper at x2", in.getLowerAt(in.getX2()) + " > " + in.getUpperAt(in.getX2()));
//			}
//		}
	}

}
