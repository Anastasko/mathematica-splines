package main;

import java.util.function.Function;

public class Line {
	
	private double k;
	
	private double m;

	public Line(double k, double m) {
		if (Double.isNaN(k)) {
			throw new IllegalArgumentException("k can't be NaN");
		}
		if (Double.isNaN(m)) {
			throw new IllegalArgumentException("m can't be NaN");
		}
		this.k = k;
		this.m = m;
	}
	
	public static Line make(Point a, Point b) {
		double k = (b.getY() - a.getY()) / (b.getX() - a.getX());
		double m = a.getY() - k * a.getX();
		return new Line(k, m);
	}
	
	public Point intersect(Line l) {
		if (l.k == k && l.m != m) {
			return null;
		}
		double xc = -(l.m - m);
		if (xc != 0) xc /= (l.k - k);
		return new Point(xc, at(xc));
	}

	public double getK() {
		return k;
	}

	public double getM() {
		return m;
	}
	
	public Line clone() {
		return new Line(k, m);
	}

	@Override
	public String toString() {
		return "{" + k + ", " + m + "}";
	}

	public double at(double x) {
		return k*x + m;
	}

	public Line unoFunc(Function<Double, Double> unoFunc) {
		return new Line(unoFunc.apply(k), unoFunc.apply(m));
	}
	
}
