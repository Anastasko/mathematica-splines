package main.func_store;

import java.util.List;

import model.Interval;
import utils.Utils;

public class X2FunctionFactory extends Utils {
	
	public static AbstractFunction create(Interval i) {
		double lower = i.getX1();
		double upper = i.getX2();
		List<Double> sp;
		List<Double> spozn;
		if (less(lower, 0) && less(0, upper)) {
			sp = list(lower, 0.0, upper);
			spozn = list(1.0, 1.0);
		} else {
			sp = list(lower, upper);
			spozn = list(1.0);
		}
		return new X2Function(
			sp,
			spozn
		);
	}

}
