package main.func_store;

import java.util.List;

import model.Interval;
import utils.Utils;

public class XFunctionFactory extends Utils {

	public static AbstractFunction create(Interval i) {
		double lower = i.getX1();
		double upper = i.getX2();
		List<Double> sp = list(lower, upper);
		List<Double> spozn = list(1.0);
		return new XFunction(
			sp,
			spozn
		);
	}
	
}
