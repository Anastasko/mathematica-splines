package main.func_store;

import java.util.List;
import java.util.stream.Collectors;

import model.Point;

public class X2Function extends AbstractFunction {

	public X2Function(List<Double> sp, List<Double> spozn) {
		super(sp, spozn, x -> x*x, x -> 2*x);
	}

	@Override
	public List<Point> getPoints() {
		return getSp().stream().map(p -> {
			return new Point(p, getY().apply(p));
		}).collect(Collectors.toList());
	}

}
