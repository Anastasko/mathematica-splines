package main.task1;

import static main.Utils.list;

public class Samples {

	private static final double Pi = Math.PI;

	public static Sample getSample1() {
		Sample s = new Sample();
		s.sp = list(-Pi, .0, Pi, 2*Pi);
		s.spozn = list(1.0, -1.0, 1.0);
		s.y = x -> - Math.sin(x);
		s.yd = x -> - Math.cos(x);
		return s;
	}
	
	public static Sample getSample2() {
		Sample s = new Sample();
		s.sp = list(-Pi, .0, 2*Pi);
		s.spozn = list(1.0, -1.0);
		s.y = x -> x*x*x;
		s.yd = x -> 3*x*x;
		return s;
	}

}
