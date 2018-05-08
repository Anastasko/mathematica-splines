package main.func_store;

import model.Interval;
import utils.Utils;

public class FunctionStore extends Utils {
	
	private Interval interval;

	public FunctionStore(double lower, double upper) {
		this.interval = new Interval(lower, upper);
	}

	public AbstractFunction sin() {
		return SinFunctionFactory.create(interval.getX1(), interval.getX2());
	}
	
	public AbstractFunction cos() {
		return CosFunctionFactory.create(interval.getX1(), interval.getX2());
	}

	public AbstractFunction x3() {
		return X3FunctionFactory.create(interval);
	}
	
	public AbstractFunction x2() {
		return X2FunctionFactory.create(interval);
	}

}
