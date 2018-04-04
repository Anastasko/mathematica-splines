package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class Intervals {

	private List<LineInterval> list = new ArrayList<>();
	
	public int size() {
		return list.size();
	}
	
	public Intervals plus(Intervals intervals) {
		return biFunc(intervals, (a,b) -> a+b);
	}
	
	public Intervals minus(Intervals intervals) {
		return biFunc(intervals, (a,b) -> a-b);
	}
	
	public Intervals div(double x) {
		return unoFunc(a -> a/x);
	}
	
	public Intervals mult(double x) {
		return unoFunc(a -> a*x);
	}
	
	public Intervals mult(Intervals intervals) {
		
		Pair<Intervals, Intervals> pair = normalize(this.addZeroes(), intervals.addZeroes());
		
		return null;
	}
	
	private Intervals unoFunc(Function<Double, Double> unoFunc) {
		Intervals res = new Intervals();
		forEach(i -> {
			Line lower = new Line(
					unoFunc.apply(i.getLower().getK()),
					unoFunc.apply(i.getLower().getM()));
			
			Line upper = new Line(
					unoFunc.apply(i.getUpper().getK()),
					unoFunc.apply(i.getUpper().getM()));
			
			res.add(new LineInterval(i.getX1(), i.getX2(), lower, upper));
		
		});
		return res;
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
			
			res.add(new LineInterval(i1.getX1(), i1.getX2(), lower, upper));
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
			if (equals(i1.getX1(), i2.getX1())) {
				one.add(i1);
				two.add(i2);
				--i;
				--j;
			} else if (less(i1.getX1(), i2.getX1())) {
				one.add(i1.setX(i2.getX1(), i2.getX2()));
				two.add(i2);
				--j;
			} else {
				one.add(i1);
				two.add(i2.setX(i1.getX1(), i1.getX2()));
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
		Intervals res = new Intervals();
		forEach(i -> {
			
			
		});
		return res;
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

	public void forEach(Consumer<LineInterval> consumer) {
		list.forEach(consumer);
	}

	public LineInterval last() {
		return list.get(size()-1);
	}
	
	
}
