package test;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import main.LinearIntervals;
import main.LinearIntervalsBuilder;
import main.QuadraticIntervals;
import main.func_store.AbstractFunction;
import main.func_store.FunctionStore;
import model.Ozn;
import model.Point;

public class Task1Test extends TestUtils {
	
	private static double LEFT = -Pi;
	private static double RIGHT = 2*Pi;
	
	private static FunctionStore store = new FunctionStore(LEFT, RIGHT);
	
	@Test
	public void testX3Splines() {
		AbstractFunction s = store.x2();
		LinearIntervals is = build(s).mult(3);
		output("x3d", is);
		QuadraticIntervals q = build(is, store.x3().getPoints());
		output("x3-splines", q);
	}

	@Test
	public void testCosSplines() {
		AbstractFunction sin = store.sin();
		LinearIntervals is = build(sin).mult(-1.0);
		output("-sin", is);
		QuadraticIntervals q = build(is, store.cos().getPoints());
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
		LinearIntervals is1 = builder.build(s1);
		LinearIntervals is2 = builder.build(s2);
		LinearIntervals is = is1.mult(5).plus(is2.div(10));
		output("x^2 div 10 + 5sin", is);
		QuadraticIntervals q = build(is, store.cos().getPoints().stream().map(p -> {
			double x = p.getX();
			return new Point(x, x*x*x / 30.0 - 5 * Math.cos(x));
		}).collect(Collectors.toList()));
		output("x^3 div 30 - 5cos", q);
	}

	private List<Double> points(AbstractFunction s1, AbstractFunction s2) {
		return union(s1.getSp(), s2.getSp());
	}

	@Test
	public void testMinus() {
		AbstractFunction s1 = store.sin();
		AbstractFunction s2 = store.x2();
		LinearIntervalsBuilder builder = builder(points(s1, s2));
		LinearIntervals is1 = builder.build(s1);
		LinearIntervals is2 = builder.build(s2);
		LinearIntervals is = is2.div(10).minus(is1.mult(5));
		output("x^2 div 10 - 5sin", is);
		QuadraticIntervals q = build(is, store.cos().getPoints().stream().map(p -> {
			double x = p.getX();
			return new Point(x, x*x*x / 30.0 + 5 * Math.cos(x));
		}).collect(Collectors.toList()));
		output("x^3 div 30 + 5cos", q);
	}
	
	@Test
	public void testTimes() {
		AbstractFunction s1 = store.sin();
		AbstractFunction s2 = store.x3();
		LinearIntervalsBuilder builder = builder(points(s1, s2));
		LinearIntervals is1 = builder.build(s1);
		LinearIntervals is2 = builder.build(s2);
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
