package test;

import org.junit.Test;

import main.LinearIntervals;
import main.LinearIntervalsBuilder;
import main.QuadraticIntervals;
import main.func_store.AbstractFunction;
import main.func_store.FunctionStore;

public class Task1Test extends TestUtils {
	
	private static double LEFT = -Pi;
	private static double RIGHT = 2*Pi;
	
	private boolean combinePoints = false;
	
	private LinearIntervals build(LinearIntervalsBuilder builder, AbstractFunction s) {
		return combinePoints ? builder.build(s) : build(s);
	}
	
	private static FunctionStore store = new FunctionStore(LEFT, RIGHT);
	
	@Test
	public void testX3Splines() {
		AbstractFunction s = store.x2();
		LinearIntervals is = build(s).mult(3);
		output("x3d", is);
		QuadraticIntervals q = build(is, x -> x*x*x);
		output("x3-splines", q);
	}

	@Test
	public void testCosSplines() {
		AbstractFunction sin = store.sin();
		LinearIntervals is = build(sin).mult(-1.0);
		output("-sin", is);
		QuadraticIntervals q = build(is, Math::cos);
		output("cos-splines", q);
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
		AbstractFunction s2 = store.x2();
		LinearIntervalsBuilder builder = builder(points(s1, s2));
		LinearIntervals is1 = build(builder, s1);
		LinearIntervals is2 = build(builder, s2);
		LinearIntervals is = is1.mult(5).plus(is2.div(10));
		output("x^2 div 10 + 5sin", is);
		QuadraticIntervals q = build(is, x -> x*x*x / 30.0 - 5 * Math.cos(x));
		output("x^3 div 30 - 5cos", q);
	}

	@Test
	public void testX2onSinPoints() {
		AbstractFunction s1 = store.sin();
		AbstractFunction s2 = store.x2();
		LinearIntervalsBuilder builder = builder(points(s1, s2));
		LinearIntervals is2 = build(builder, s2);
		LinearIntervals is = is2.div(10);
		output("x^2 div 10 on sin", is);
	}

	@Test
	public void testMinus() {
		AbstractFunction s1 = store.sin();
		AbstractFunction s2 = store.x2();
		LinearIntervalsBuilder builder = builder(points(s1, s2));
		LinearIntervals is1 = build(builder, s1);
		LinearIntervals is2 = build(builder, s2);
		LinearIntervals is = is2.div(10).minus(is1.mult(5));
		output("x^2 div 10 - 5sin", is);
		QuadraticIntervals q = build(is, x -> x*x*x / 30.0 + 5 * Math.cos(x));
		output("x^3 div 30 + 5cos", q);
	}
	
	@Test
	public void testTimes() {
		AbstractFunction s1 = store.sin();
		AbstractFunction s2 = store.x2();
		LinearIntervalsBuilder builder = builder(points(s1, s2));
		LinearIntervals is1 = build(builder, s1);
		LinearIntervals is2 = build(builder, s2);
		LinearIntervals res = is1.mult(is2).mult(-1.0);
		output("x^2 times -sin", res);
		QuadraticIntervals q = build(res, x -> (x*x - 2) * Math.cos(x) - 2*x*Math.sin(x));
		output("(x^2-2) cos - 2x sin", q);
	}

	@Test
	public void testDiv() {
		AbstractFunction s1 = store.sin();
		AbstractFunction s2 = store.x2();
		LinearIntervalsBuilder builder = builder(points(s1, s2));
		LinearIntervals is1 = build(builder, s1).plus(1.2);
		LinearIntervals is2 = build(builder, s2).div(2.0);
		output("x^2 div 2 sin inc 2", is2.div(is1));
	}
	
	@Test
	public void testDiv2() {
		AbstractFunction s1 = store.cos();
		AbstractFunction s2 = store.sin();
		LinearIntervalsBuilder builder = builder(points(s1, s2));
		LinearIntervals is1 = build(builder, s1).plus(1.2);
		LinearIntervals is2 = build(builder, s2);
		output("sin div (cos + 1.2)", is2.div(is1));
	}
	
}
