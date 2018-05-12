package main.func_store;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import model.Ozn;
import model.Point;
import utils.Utils;

public abstract class AbstractFunction extends Utils {
	
	private Ozn ozn;
	private Function<Double, Double> y;
	private Function<Double, Double> yd;
	
	public AbstractFunction(List<Double> sp, List<Double> spozn, Function<Double, Double> y, Function<Double, Double> yd) {
		this.ozn = new Ozn(sp, spozn);
		this.y = y;
		this.yd = yd;
	}

	public List<Double> getSp() {
		return ozn.getSp();
	}

	public List<Double> getSpozn() {
		return ozn.getSpozn();
	}
	
	public Ozn getOzn() {
		return ozn;
	}

	public Function<Double, Double> getY() {
		return y;
	}

	public Function<Double, Double> getYd() {
		return yd;
	}

	@Override
	public String toString() {
		return "Function [sp=" + getSp() + ", spozn=" + getSpozn() + ", y=" + y + ", yd=" + yd + "]";
	}

	public List<Point> getPoints() {
		return getSp().stream().map(p -> {
			return new Point(p, getY().apply(p));
		}).collect(Collectors.toList());
	}
	
	public List<Double> getSpozn(List<Double> points) {
		List<Double> result = new ArrayList<>();
		int j = 0;
		for(Double p : points) {
			if (!less(p, getSp().get(j))) ++j;
			if (j == getSp().size()) continue;
			result.add(getSpozn().get(j-1));
		}
		return result;
	}
	
}
