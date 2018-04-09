package test;

import org.junit.Test;

import main.Intervals;
import main.task1.AbstractFunction;
import main.task1.FunctionStore;

public class Task1Test extends TestUtils {
	
	private static FunctionStore store = new FunctionStore(-Pi, 2*Pi);
	
	@Test
	public void testSin() {
		AbstractFunction sin = store.sin();
		Intervals is = func(sin);
		output("-sin", is.mult(-1.0));
	}

	@Test
	public void testX3() {
		AbstractFunction s = store.x3();
		Intervals is = func(s);
		output("x3", is);
	}

	@Test
	public void testPlus() {
		AbstractFunction s1 = store.sin();
		AbstractFunction s2 = store.x3();
		Intervals is1 = func(s1);
		Intervals is2 = func(s2);
		output("x^3 div 20 - 5sin", is1.mult(5).plus(is2.div(20)));
	}

	@Test
	public void testTimes() {
		AbstractFunction s1 = store.sin();
		AbstractFunction s2 = store.x3();
		Intervals is1 = func(s1);
		Intervals is2 = func(s2);
		output("x^3 times -sin", is1.mult(is2).mult(-1.0));
	}

}
