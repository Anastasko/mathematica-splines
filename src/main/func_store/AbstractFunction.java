package main.func_store;

import java.util.List;
import java.util.function.Function;

import utils.Utils;

public class AbstractFunction extends Utils {
	
	private List<Double> sp;
	private List<Double> spozn;
	private Function<Double, Double> y;
	private Function<Double, Double> yd;
	
	public AbstractFunction(List<Double> sp, List<Double> spozn, Function<Double, Double> y, Function<Double, Double> yd) {
		this.sp = sp;
		this.spozn = spozn;
		this.y = y;
		this.yd = yd;
	}

	public List<Double> getSp() {
		return sp;
	}

	public List<Double> getSpozn() {
		return spozn;
	}

	public Function<Double, Double> getY() {
		return y;
	}

	public Function<Double, Double> getYd() {
		return yd;
	}

	@Override
	public String toString() {
		return "Function [sp=" + sp + ", spozn=" + spozn + ", y=" + y + ", yd=" + yd + "]";
	}
	
}
