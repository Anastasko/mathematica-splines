package main.func_store;

import java.util.List;

public class CosFunction extends AbstractFunction {

	public CosFunction(List<Double> sp, List<Double> spozn) {
		super(sp, spozn, x -> Math.cos(x), x -> -Math.sin(x));
	}

}
