package test;

import org.junit.Test;

import main.LinearIntervals;
import main.QuadraticIntervals;
import main.func_store.AbstractFunction;
import main.func_store.FunctionStore;

public class Task1Test extends TestUtils {
	
	private static FunctionStore store = new FunctionStore(-Pi, 2*Pi);
	
	@Test
	public void testX3Splines() {
		AbstractFunction s = store.x2();
		LinearIntervals is = build(s);
		QuadraticIntervals q = build(is.mult(3));
		output("x3-splines", q);
	}

	@Test
	public void testSin() {
		AbstractFunction sin = store.sin();
		LinearIntervals is = build(sin);
		output("-sin", is.mult(-1.0));
	}

	@Test
	public void testX3() {
		AbstractFunction s = store.x3();
		LinearIntervals is = build(s);
		output("x3", is);
	}
	
	@Test
	public void testX2() {
		AbstractFunction s = store.x2();
		LinearIntervals is = build(s);
		output("x2", is);
	}
	
	@Test
	public void testPlus() {
		AbstractFunction s1 = store.sin();
		AbstractFunction s2 = store.x3();
		LinearIntervals is1 = build(s1);
		LinearIntervals is2 = build(s2);
		output("x^3 div 20 + 5sin", is1.mult(5).plus(is2.div(20)));
	}

	@Test
	public void testMinus() {
		AbstractFunction s1 = store.sin();
		AbstractFunction s2 = store.x3();
		LinearIntervals is1 = build(s1);
		LinearIntervals is2 = build(s2);
		output("x^3 div 20 - 5sin", is2.div(20).minus(is1.mult(5)));
	}
	
	@Test
	public void testTimes() {
		AbstractFunction s1 = store.sin();
		AbstractFunction s2 = store.x3();
		LinearIntervals is1 = build(s1);
		LinearIntervals is2 = build(s2);
		output("x^3 times -sin", is1.mult(is2).mult(-1.0));
	}

	@Test
	public void testDiv() {
		AbstractFunction s1 = store.sin();
		AbstractFunction s2 = store.x3();
		LinearIntervals is1 = build(s1).plus(1.2);
		LinearIntervals is2 = build(s2).div(12.0);
		output("x^3 div sin inc 2", is2.div(is1));
	}
	
}
