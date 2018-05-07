package model;

import java.util.function.Function;

import utils.Utils;

public class Parabola extends Utils {
	
	private double a, b, c;

	public Parabola(double a, double b, double c) {
		if (Double.isNaN(a)) {
			throw new IllegalArgumentException("a can't be NaN");
		}
		if (Double.isNaN(b)) {
			throw new IllegalArgumentException("b can't be NaN");
		}
		if (Double.isNaN(c)) {
			throw new IllegalArgumentException("c can't be NaN");
		}
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	public static Parabola make(Point a, Point b) {
		
		return new Parabola(0,0,0);
	}
	
	public Point intersect(Parabola l) {
		
		return new Point(0,0);
	}
	
	public boolean equals(Parabola l) {
		return equals(l.a, a) && equals(l.b, b) && equals(l.c, c);
	}

	public double getA() {
		return a;
	}

	public double getB() {
		return b;
	}
	
	public double getC() {
		return c;
	}
	
	public Parabola clone() {
		return new Parabola(a, b, c);
	}

	@Override
	public String toString() {
		return "{" + a + ", " + b + ", " + c + "}";
	}

	public double at(double x) {
		return a*x*x + b*x + b;
	}

	public Parabola unoFunc(Function<Double, Double> unoFunc) {
		return new Parabola(unoFunc.apply(a), unoFunc.apply(b), unoFunc.apply(c));
	}
	
	public Parabola up(Double up) {
		return new Parabola(a, b, c + up);
	}
	
}
