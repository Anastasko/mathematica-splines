package main.func_store;

import java.util.List;

public class X2Function extends AbstractFunction {

	public X2Function(List<Double> sp, List<Double> spozn) {
		super(sp, spozn, x -> x*x, x -> 2*x);
	}

}
