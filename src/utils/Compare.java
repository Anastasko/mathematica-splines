package utils;

import main.Const;

public class Compare {

	public static boolean less(double x1, double x2) {
		return x1 + Const.EPS < x2;
	}
	
	public static boolean greater(double x1, double x2) {
		return less(x2, x1);
	}

	public static boolean equals(double x1, double x2) {
		return !less(x1, x2) && !greater(x1, x2);
	}
	
}
