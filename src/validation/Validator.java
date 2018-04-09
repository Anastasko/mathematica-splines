package validation;

import java.util.List;

import main.Intervals;
import main.Line;
import main.LineInterval;
import main.MyException;
import utils.Utils;

public class Validator extends Utils {
	
	public static MyException validate(List<LineInterval> list) {
		for(int i=1; i<list.size(); ++i) {
			double p = list.get(i).getX1();
			double pp = list.get(i-1).getX1();
			if (less(p, pp)) {
				return fail(p + " < " + pp);
			}
		}
		for(LineInterval li : list) {
			if (validate(li) != null) return validate(li);
		}
		return null;
	}
	
	public static MyException validate(LineInterval li) {
		if (Double.isNaN(li.getX1())) return fail("x1 is NaN");
		if (Double.isNaN(li.getX2())) return fail("x2 is NaN");
		if (validate(li.getLine()) != null) return validate(li.getLine());
		if (less(li.getX2(), li.getX1())) return fail("x2 < x1");
		return null;
	}

	public static MyException validate(Line l) {
		if (Double.isNaN(l.getK())) return fail("k is NaN");
		if (Double.isNaN(l.getM())) return fail("m is NaN");
		return null;
	}

	public static MyException validate(Intervals i) {
		if (validate(i.getLower()) != null) return validate(i.getLower());
		if (validate(i.getUpper()) != null) return validate(i.getUpper());
		return null;
	}

}
