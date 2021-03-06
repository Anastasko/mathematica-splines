package model;

import utils.Utils;

public class Interval extends Utils {
	
	private double x1;
	
	private double x2;
	
	public Interval(double x1, double x2) {
		if (!less(x1, x2)) fail("Bad Interval", "not x1 < x2", "x1=" + x1, "x2=" + x2);
		this.x1 = x1;
		this.x2 = x2;
	}

	public double getX1() {
		return x1;
	}
	
	public double getX2() {
		return x2;
	}
	
	public boolean contains(double x) {
		return !less(x, x1) && !greater(x, x2);
	}

	public double ratio(double r) {
		if (r < 0 || r > 1) fail("bad arg exception");
		return x1 + (x2 - x1) * r;
	}

}
