package main.task1;

import utils.Utils;

public class Samples extends Utils {

	private static final double Pi = Math.PI;
	
	private static final double Infinity = Double.POSITIVE_INFINITY;

	public static Sample getSample1() {
		Sample s = new Sample();
		s.sp = list(-Pi, -Pi/2, .0, Pi/2, Pi, 1.5*Pi, 2*Pi);
		s.spozn = list(1.0, 1.0, -1.0, -1.0, 1.0, 1.0);
		s.y = x -> -Math.sin(x);
		s.yd = x -> -Math.cos(x);
		return s;
	}
	
	public static Sample getSampleX3() {
		Sample s = new Sample();
		s.sp = list(-Pi, .0, 2*Pi);
		s.spozn = list(1.0, -1.0);
		s.y = x -> x*x*x;
		s.yd = x -> 3*x*x;
		return s;
	}

	public static Sample getSampleX2() {
		Sample s = new Sample();
		s.sp = list(-Infinity, .0, +Infinity);
		print(s.sp);
		s.spozn = list(1.0, 1.0);
		s.y = x -> x*x;
		s.yd = x -> 2*x;
		return s;
	}

}
