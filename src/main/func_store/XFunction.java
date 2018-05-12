package main.func_store;

import java.util.List;

public class XFunction extends AbstractFunction {

	public XFunction(List<Double> sp, List<Double> spozn) {
		super(sp, spozn, x -> x, x -> 1.0);
	}

}
