package main.func_store;

import java.util.List;

public class SinFunction extends AbstractFunction {

	public SinFunction(List<Double> sp, List<Double> spozn) {
		super(sp, spozn, x -> Math.sin(x), x -> Math.cos(x));
	}

}
