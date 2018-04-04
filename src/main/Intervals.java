package main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class Intervals extends Utils {

	private List<LineInterval> lower = new ArrayList<>();
	
	private List<LineInterval> upper = new ArrayList<>();
	
	public List<LineInterval> getLower() {
		return lower;
	}

	public List<LineInterval> getUpper() {
		return upper;
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
		Intervals res = new Intervals();
//		for(int i=0; i<pair.getOne().size(); ++i) {
//			
//			LineInterval f = pair.getOne().get(i);
//			LineInterval g = pair.getTwo().get(i);
//			
//			double x1 = f.getX1();
//			double x2 = f.getX2();
//			
//			double gLowerMin = Math.min(g.getLower().at(x1), g.getLower().at(x2));
//			double fLowerMin = Math.min(f.getLower().at(x1), f.getLower().at(x2));
//			double gUpperMax = Math.max(g.getUpper().at(x1), g.getUpper().at(x2));
//			double fUpperMax = Math.max(f.getUpper().at(x1), f.getUpper().at(x2));
//
//			if (0 <= fLowerMin && 0 <= gLowerMin) {
//				res.addAll(combine(f.getLower(), g.getLower(), f.getUpper(), g.getUpper(), x1, x2));
//			}
//			if (fUpperMax <= 0 && 0 <= gLowerMin) {
//				res.addAll(combine(f.getLower(), g.getUpper(), f.getUpper(), g.getLower(), x1, x2));
//			}
//			if ((fUpperMax > 0 && 0 > fLowerMin) && 0 <= gLowerMin) {
//				res.addAll(combine(f.getLower(), g.getUpper(), f.getUpper(), g.getUpper(), x1, x2));
//			}
//
//			if (0 <= fLowerMin && 0 >= gUpperMax) {
//				res.addAll(combine(f.getUpper(), g.getLower(), f.getLower(), g.getUpper(), x1, x2));
//			}
//			if (fUpperMax <= 0 && 0 >= gUpperMax) {
//				res.addAll(combine(f.getUpper(), g.getUpper(), f.getLower(), g.getLower(), x1, x2));
//			}
//			if ((fUpperMax > 0 && 0 > fLowerMin) && 0 >= gUpperMax) {
//				res.addAll(combine(f.getUpper(), g.getLower(), f.getLower(), g.getLower(), x1, x2));
//			}
//
//			if (0 <= fLowerMin && (gUpperMax > 0 && 0 > gLowerMin)) {
//				res.addAll(combine(f.getUpper(), g.getLower(), f.getUpper(), g.getUpper(), x1, x2));
//			}
//			if (fUpperMax <= 0 && (gUpperMax > 0 && 0 > gLowerMin)) {
//				res.addAll(combine(f.getLower(), g.getUpper(), f.getLower(), g.getLower(), x1, x2));
//			}
//			if ((fUpperMax > 0 && 0 > fLowerMin) && (gUpperMax > 0 && 0 > gLowerMin)) {
//				throw new NotImplementedException();
//				// res.addAll(combine2(f,g));
//			}
//		}
		return res;
	}
	
	private Collection<LineInterval> combine(Line lower1, Line lower2, Line upper1, Line upper2, double x1, double x2) {
		
		return null;
	}

	private Intervals unoFunc(Function<Double, Double> unoFunc) {
		Intervals res = new Intervals();
		lowerUpper(lu -> {
			lu.apply(this).forEach(i -> {
				Line line = new Line(
						unoFunc.apply(i.getLine().getK()),
						unoFunc.apply(i.getLine().getM()));
				lu.apply(res).add(new LineInterval(i.getX1(), i.getX2(), line));
			});
		});
		return res;
	}
	
	private Intervals biFunc(Intervals intervals, BiFunction<Double, Double, Double> biFunc) {
		Intervals res = new Intervals();
		Pair<Intervals, Intervals> pair = normalize(this, intervals);
		lowerUpper(lu -> {
			biForEach(lu, pair).forEach((i1, i2) -> {
				Line line = new Line(
						biFunc.apply(i1.getLine().getK(), i2.getLine().getK()),
						biFunc.apply(i1.getLine().getM(), i2.getLine().getM()));
				lu.apply(res).add(new LineInterval(i1.getX1(), i1.getX2(), line));
			});
		});
		return res;
	}
	
	private BiIterable<LineInterval> biForEach(
			Function<Intervals, List<LineInterval>> lu,
			Pair<Intervals, Intervals> pair) {
		List<LineInterval> list1 = lu.apply(pair.getOne());
		List<LineInterval> list2 = lu.apply(pair.getTwo());
		return new BiIterable<LineInterval>(list1, list2);
	}

	/**
	 * на вхід два обмежники
	 * на вихід ті ж обмежники зі всіма точками (точки взяті з одного в інший і навпаки)
	 */
	private static Pair<Intervals, Intervals> normalize(Intervals A, Intervals B) {
		Intervals one = new Intervals();
		Intervals two = new Intervals();
		lowerUpper(lu -> {
			List<LineInterval> a = lu.apply(A);
			List<LineInterval> b = lu.apply(B);
			int i = a.size() - 1;
			int j = b.size() - 1;
			while (i >= 0 || j >= 0) {
				LineInterval i1 = a.get(i);
				LineInterval i2 = b.get(j);
				if (equals(i1.getX1(), i2.getX1())) {
					lu.apply(one).add(i1);
					lu.apply(two).add(i2);
					--i;
					--j;
				} else if (less(i1.getX1(), i2.getX1())) {
					lu.apply(one).add(i1.withX(i2.getX1(), i2.getX2()));
					lu.apply(two).add(i2);
					--j;
				} else {
					lu.apply(one).add(i1);
					lu.apply(two).add(i2.withX(i1.getX1(), i1.getX2()));
					--i;
				}
			}
		});
		one.reverse();
		two.reverse();
		return new Pair<>(one, two);
	}

	/**
	 * на вхід @this обмежник
	 * на вихід той же обмежник з доданими нуль-точками
	 */
	private Intervals addZeroes() {
		Intervals res = new Intervals();
		lowerUpper(lu -> {
			forEach(lu.apply(this), i -> {
				Point p = i.getIntersectZero();
				List<LineInterval> list = lu.apply(res);
				if (p == null) {
					list.add(i);
				} else {
					list.add(i.withX(i.getX1(), p.getX()));
					list.add(i.withX(p.getX(), i.getX2()));
				}
			});
		});
		return res;
	}

	public void addLower(LineInterval i) {
		lower.add(i);
	}
	
	public void addAllLower(Collection<LineInterval> i) {
		lower.addAll(i);
	}
	
	public void addUpper(LineInterval i) {
		upper.add(i);
	}
	
	public void addAllUpper(Collection<LineInterval> i) {
		upper.addAll(i);
	}
	
	private void reverse() {
		Collections.reverse(lower);
		Collections.reverse(upper);
	}

	private static boolean less(double x, double x2) {
		return x + Const.EPS < x2;
	}

	private static boolean equals(double x, double x2) {
		return Math.abs(x2-x) < Const.EPS;
	}

	@Override
	public String toString() {
		return "Intervals " + "\nlower = " + lower + " \nupper = " + upper;
	}

	public void forEach(List<LineInterval> is, Consumer<LineInterval> consumer) {
		is.forEach(consumer);
	}

	public LineInterval lastUpper() {
		return upper.get(upper.size()-1);
	}
	
	public LineInterval lastLower() {
		return lower.get(lower.size()-1);
	}
	
}
