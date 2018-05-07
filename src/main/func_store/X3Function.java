package main.func_store;

import java.util.List;

public class X3Function extends AbstractFunction {

	public X3Function(List<Double> sp, List<Double> spozn) {
		super(sp, spozn, x -> x*x*x, x -> 3*x*x);
	}

}
