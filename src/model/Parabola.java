package model;

import java.util.List;
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
		return a*x*x + b*x + c;
	}

	public Parabola unoFunc(Function<Double, Double> unoFunc) {
		return new Parabola(unoFunc.apply(a), unoFunc.apply(b), unoFunc.apply(c));
	}
	
	public Parabola up(Double up) {
		return new Parabola(a, b, c + up);
	}
	
	public List<Point> cross(Parabola p) {
		return new Parabola(a - p.getA(), b - p.getB(), c - p.getC()).findRoots(p::at);
	}
	
	public List<Point> findRoots() {
		return findRoots(this::at);
	}

	private List<Point> findRoots(Function<Double, Double> at) {
		if (equals(a, 0)) return new Line(b, c).findRoots();
		double D = b*b - 4*a*c;
		if (equals(D, 0)) {
			double p = -b / (2*a);
			return list(new Point(p, at(p)));
		}
		if (D < 0) return list();
		double sqrtD = Math.sqrt(D);
		double p1 = (-b + sqrtD) / (2*a);
		double p2 = (-b - sqrtD) / (2*a);
		if (p1 > p2) {
			double temp = p1;
			p1 = p2;
			p2 = temp;
		}
		return list(new Point(p1, at.apply(p1)), new Point(p2, at.apply(p2)));
	}
	
}
