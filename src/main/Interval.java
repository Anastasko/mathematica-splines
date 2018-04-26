package main;

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

}
