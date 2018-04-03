package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

public class Intervals {

	private List<LineInterval> list = new ArrayList<>();
	
	public int size() {
		return list.size();
	}
	
	public Intervals add(Intervals intervals) {
		return biFunc(intervals, (a,b) -> a+b);
	}
	
	public Intervals minus(Intervals intervals) {
		return biFunc(intervals, (a,b) -> a-b);
	}
	
	private Intervals biFunc(Intervals intervals, BiFunction<Double, Double, Double> biFunc) {
		Intervals res = new Intervals();
		Pair<Intervals, Intervals> pair = normalize(this, intervals);
		int i = 0;
		while (i < pair.getOne().size()) {
			LineInterval i1 = pair.getOne().get(i);
			LineInterval i2 = pair.getTwo().get(i);
			
			Line lower = new Line(
					biFunc.apply(i1.getLower().getK(), i2.getLower().getK()),
					biFunc.apply(i1.getLower().getM(), i2.getLower().getM()));
			
			Line upper = new Line(
					biFunc.apply(i1.getUpper().getK(), i2.getUpper().getK()),
					biFunc.apply(i1.getUpper().getM(), i2.getUpper().getM()));
			
			res.add(new LineInterval(i1.getX(), lower, upper));
			++i;
		}
		return res;
	}
	
	private Pair<Intervals, Intervals> normalize(Intervals a, Intervals b) {
		Intervals one = new Intervals();
		Intervals two = new Intervals();
		int i = a.size() - 1;
		int j = b.size() - 1;
		while (i >= 0 || j >= 0) {
			LineInterval i1 = a.get(i);
			LineInterval i2 = b.get(j);
			if (equals(i1.getX(), i2.getX())) {
				one.add(i1);
				two.add(i2);
				--i;
				--j;
			} else if (less(i1.getX(), i2.getX())) {
				one.add(i1.setX(i2.getX()));
				two.add(i2);
				--j;
			} else {
				one.add(i1);
				two.add(i2.setX(i1.getX()));
				--i;
			}
		}
		one.reverse();
		two.reverse();
		return new Pair<>(one, two);
	}
	
	
	public void add(LineInterval i) {
		list.add(i);
	}
	
	private LineInterval get(int pos) {
		return list.get(pos);
	}
	
	private void reverse() {
		Collections.reverse(list);
	}
	
	private Intervals addZeroes() {
		Intervals i = new Intervals();
		
		return i;
	}

	private boolean less(double x, double x2) {
		return x + Const.EPS < x2;
	}

	private boolean equals(double x, double x2) {
		return Math.abs(x2-x) < Const.EPS;
	}

	@Override
	public String toString() {
		return "Intervals " + list;
	}
	
	
}
