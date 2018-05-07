package main.func_store;

import java.util.List;

import model.Point;

public class SinFunction extends AbstractFunction {

	public SinFunction(List<Double> sp, List<Double> spozn) {
		super(sp, spozn, x -> Math.sin(x), x -> Math.cos(x));
	}

	@Override
	public List<Point> getPoints() {
		return list();
	}

}
