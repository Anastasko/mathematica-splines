package main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import model.Interval;
import model.Line;
import model.LineInterval;
import model.LinesInterval;
import model.Pair;
import model.Point;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import utils.BiIterable;
import utils.Utils;

public class LinearIntervalsImpl extends Utils implements LinearIntervals {
	
	private Interval interval;

	private List<LineInterval> lower = new ArrayList<>();
	
	private List<LineInterval> upper = new ArrayList<>();
	
	public LinearIntervalsImpl(Interval interval) {
		this.interval = interval;
	}

	public List<LineInterval> getLower() {
		return lower;
	}

	public List<LineInterval> getUpper() {
		return upper;
	}

	public LinearIntervals plus(LinearIntervals intervals) {
		return biFunc(intervals, (a,b) -> a+b);
	}
	
	public LinearIntervals minus(LinearIntervals intervals) {
		return plus(intervals.mult(-1.0));
	}
	
	public LinearIntervals div(double x) {
		return unoFunc(a -> a/x, x < 0);
	}
	
	public LinearIntervals mult(double x) {
		return unoFunc(a -> a*x, x < 0);
	}
	
	private void swapLowerUpper() {
		List<LineInterval> temp = lower;
		lower = upper;
		upper = temp;
	}
	
	public LinearIntervals mult(LinearIntervals intervals) {
		Pair<LinearIntervals, LinearIntervals> pair = normalize(this.addZeroes(), intervals.addZeroes());
		LinearIntervalsImpl res = empty();
		biForEach(pair).forEach((f,g) -> {
			
			double x1 = f.getX1();
			double x2 = f.getX2();
			
			double gLowerMin = Math.min(g.getLower().at(x1), g.getLower().at(x2));
			double fLowerMin = Math.min(f.getLower().at(x1), f.getLower().at(x2));
			double gUpperMax = Math.max(g.getUpper().at(x1), g.getUpper().at(x2));
			double fUpperMax = Math.max(f.getUpper().at(x1), f.getUpper().at(x2));
			boolean debug = true;
			if (0 <= fLowerMin && 0 <= gLowerMin) {
				print(debug, "11");
				res.addAllLower(combineMult(f.getLower(), g.getLower(), x1, x2, x -> x.getLower()));
				res.addAllUpper(combineMult(f.getUpper(), g.getUpper(), x1, x2, x -> x.getUpper()));
			}
			if (fUpperMax <= 0 && 0 <= gLowerMin) {
				print(debug, "21");
				res.addAllLower(combineMult(f.getLower(), g.getUpper(), x1, x2, x -> x.getLower()));
				res.addAllUpper(combineMult(f.getUpper(), g.getLower(), x1, x2, x -> x.getUpper()));
			}
			if ((fUpperMax > 0 && 0 > fLowerMin) && 0 <= gLowerMin) {
				print(debug, "31");
				res.addAllLower(combineMult(f.getLower(), g.getUpper(), x1, x2, x -> x.getLower()));
				res.addAllUpper(combineMult(f.getUpper(), g.getUpper(), x1, x2, x -> x.getUpper()));
			}

			if (0 <= fLowerMin && 0 >= gUpperMax) {
				print(debug, "12");
				res.addAllLower(combineMult(f.getUpper(), g.getLower(), x1, x2, x -> x.getLower()));
				res.addAllUpper(combineMult(f.getLower(), g.getUpper(), x1, x2, x -> x.getUpper()));
			}
			if (fUpperMax <= 0 && 0 >= gUpperMax) {
				print(debug, "22");
				res.addAllLower(combineMult(f.getUpper(), g.getUpper(), x1, x2, x -> x.getLower()));
				res.addAllUpper(combineMult(f.getLower(), g.getLower(), x1, x2, x -> x.getUpper()));
			}
			if ((fUpperMax > 0 && 0 > fLowerMin) && 0 >= gUpperMax) {
				print(debug, "23");
				res.addAllLower(combineMult(f.getUpper(), g.getLower(), x1, x2, x -> x.getLower()));
				res.addAllUpper(combineMult(f.getLower(), g.getLower(), x1, x2, x -> x.getUpper()));
			}

			if (0 <= fLowerMin && (gUpperMax > 0 && 0 > gLowerMin)) {
				print(debug, "13");
				res.addAllLower(combineMult(f.getUpper(), g.getLower(), x1, x2, x -> x.getLower()));
				res.addAllUpper(combineMult(f.getUpper(), g.getUpper(), x1, x2, x -> x.getUpper()));
			}
			if (fUpperMax <= 0 && (gUpperMax > 0 && 0 > gLowerMin)) {
				print(debug, "23");
				res.addAllLower(combineMult(f.getLower(), g.getUpper(), x1, x2, x -> x.getLower()));
				res.addAllUpper(combineMult(f.getLower(), g.getLower(), x1, x2, x -> x.getUpper()));
			}
			if ((fUpperMax > 0 && 0 > fLowerMin) && (gUpperMax > 0 && 0 > gLowerMin)) {
				print(debug, "33");
				throw new NotImplementedException();
				// res.addAll(combine2(f,g));
			}
		});
		return res;
	}

	private List<LineInterval> combineMult(Line l1, Line l2, double x1, double x2, Function<LinearIntervals, List<LineInterval>> lu) {
		if (equals(l1.getK(), .0)) {
			return list(new LineInterval(x1, x2, l2.unoFunc(x -> x*l1.getM())));
		}
		if (equals(l2.getK(), .0)) {
			return list(new LineInterval(x1, x2, l1.unoFunc(x -> x*l2.getM())));
		}
		
		final Function<Double, Double> func = x -> l1.at(x) * l2.at(x);
		
		final Function<Double, Double> funcd = x -> 2 * l1.getK() * l2.getK() * x +
			     l1.getK() * l2.getM() +
			     l1.getM() * l2.getK();
		
		Point i = new Point(
				-l1.getK() * l2.getM() +
			     l1.getM() * l2.getK() / 
			     (2 * l1.getK() * l2.getK()), 0);
		if (less(i.getX(), x2) && greater(i.getX(), x1)) {
			
			return lu.apply(LinearIntervalsBuilder.build(
					list(x1, i.getX(), x2),
					list(l1.getK() * l2.getK(), l1.getK() * l2.getK()),
					func,
					funcd
				));
		}
		
		return lu.apply(LinearIntervalsBuilder.build(
				list(x1, x2),
				list(l1.getK() * l2.getK()),
				func,
				funcd
			));
	}

	private LinearIntervals unoFunc(Function<Double, Double> unoFunc, boolean swap) {
		LinearIntervalsImpl res = empty();
		lowerUpper(lu -> {
			lu.apply(this).forEach(i -> {
				Line line = i.getLine().unoFunc(unoFunc);
				lu.apply(res).add(new LineInterval(i.getX1(), i.getX2(), line));
			});
		});
		if (swap) res.swapLowerUpper();
		return res;
	}
	
	private LinearIntervals biFunc(LinearIntervals intervals, BiFunction<Double, Double, Double> biFunc) {
		LinearIntervals res = empty();
		Pair<LinearIntervals, LinearIntervals> pair = normalize(this, intervals);
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
			Function<LinearIntervals, List<LineInterval>> lu,
			Pair<LinearIntervals, LinearIntervals> pair) {
		List<LineInterval> list1 = lu.apply(pair.getOne());
		List<LineInterval> list2 = lu.apply(pair.getTwo());
		return new BiIterable<LineInterval>(list1, list2);
	}
	
	private BiIterable<LinesInterval> biForEach(Pair<LinearIntervals, LinearIntervals> pair) {
		List<LinesInterval> list1 = pair.getOne().intervals();
		List<LinesInterval> list2 = pair.getTwo().intervals();
		return new BiIterable<LinesInterval>(list1, list2);
	}

	public List<LinesInterval> intervals() {
		BiIterable<LineInterval> iterable = new BiIterable<>(getLower(), getUpper());
		List<LinesInterval> res = new ArrayList<>();
		iterable.forEach((lower, upper) -> {
			res.add(new LinesInterval(lower.getX1(), lower.getX2(), lower.getLine(), upper.getLine()));
		});
		return res;
	}

	/**
	 * на вхід два обмежники
	 * на вихід ті ж обмежники зі всіма точками (точки взяті з одного в інший і навпаки)
	 */
	private static Pair<LinearIntervals, LinearIntervals> normalize(LinearIntervals A, LinearIntervals B) {
		LinearIntervalsImpl one = new LinearIntervalsImpl(A.getInterval());
		LinearIntervalsImpl two = new LinearIntervalsImpl(B.getInterval());
		lowerUpper(lu -> {
			List<LineInterval> a = lu.apply(A);
			List<LineInterval> b = lu.apply(B);
			int i = a.size() - 1;
			int j = b.size() - 1;
			double prev = a.get(i).getX2();
			while (i >= 0 || j >= 0) {
				LineInterval i1 = a.get(i);
				LineInterval i2 = b.get(j);
				if (equals(i1.getX1(), i2.getX1())) {
					lu.apply(one).add(i1.withX2(prev));
					lu.apply(two).add(i2.withX2(prev));
					prev = i1.getX1();
					--i;
					--j;
				} else if (less(i1.getX1(), i2.getX1())) {
					lu.apply(one).add(i1.withX(i2.getX1(), prev));
					lu.apply(two).add(i2.withX2(prev));
					prev = i2.getX1();
					--j;
				} else {
					lu.apply(one).add(i1.withX2(prev));
					lu.apply(two).add(i2.withX(i1.getX1(), prev));
					prev = i1.getX1();
					--i;
				}
			}
		});
		one.reverse();
		two.reverse();
		return new Pair<>(one, two);
	}
	
	/**
	 * на вхід обмежник @this
	 * на вихід обмежник зі всіма точками (точки взяті з lower в upper і навпаки)
	 */
	@Override
	public LinearIntervals normalize() {
		LinearIntervalsImpl res = new LinearIntervalsImpl(getInterval());
		List<LineInterval> a = getLower();
		List<LineInterval> b = getUpper();
		int i = a.size() - 1;
		int j = b.size() - 1;
		double prev = a.get(i).getX2();
		while (i >= 0 || j >= 0) {
			LineInterval i1 = a.get(i);
			LineInterval i2 = b.get(j);
			if (equals(i1.getX1(), i2.getX1())) {
				res.getLower().add(i1.withX2(prev));
				res.getUpper().add(i2.withX2(prev));
				prev = i1.getX1();
				--i;
				--j;
			} else if (less(i1.getX1(), i2.getX1())) {
				res.getLower().add(i1.withX(i2.getX1(), prev));
				res.getUpper().add(i2.withX2(prev));
				prev = i2.getX1();
				--j;
			} else {
				res.getLower().add(i1.withX2(prev));
				res.getUpper().add(i2.withX(i1.getX1(), prev));
				prev = i1.getX1();
				--i;
			}
		}
		res.reverse();
		return res;
	}

	/**
	 * на вхід @this обмежник
	 * на вихід той же обмежник з доданими нуль-точками
	 */
	public LinearIntervals addZeroes() {
		LinearIntervals res = empty();
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

	private LinearIntervalsImpl empty() {
		return new LinearIntervalsImpl(getInterval());
	}
	
	private void addAllLower(Collection<LineInterval> i) {
		i.forEach(one -> addLower(one));
	}
	
	private void addAllUpper(Collection<LineInterval> i) {
		i.forEach(one -> addUpper(one));
	}
	
	public void addLower(LineInterval i) {
		add(l -> l.getLower(), "lower", i);
	}
	
	public void addUpper(LineInterval i) {
		add(l -> l.getUpper(), "upper", i);
	}
	
	private void add(Function<LinearIntervals, List<LineInterval>> lu, String name, LineInterval i) {
		List<LineInterval> list = lu.apply(this);
		if (list.size() > 0 && less(i.getX1(), last(list).getX1())) {
			fail("how", "i.x1=" + i.getX1(), "last(" + name + ").x1=" + last(list).getX1());
		}
		list.add(i);
	}
	
	private void reverse() {
		Collections.reverse(lower);
		Collections.reverse(upper);
	}

	@Override
	public String toString() {
		return "Intervals " + "\nlower = " + lower + " \nupper = " + upper;
	}

	public void forEach(List<LineInterval> is, Consumer<LineInterval> consumer) {
		is.forEach(consumer);
	}
	
	public Interval getInterval() {
		return interval;
	}

	@Override
	public LinearIntervals plus(double d) {
		LinearIntervals res = empty();
		lowerUpper(lu -> {
			lu.apply(this).forEach(i -> {
				Line line = i.getLine().up(d);
				lu.apply(res).add(new LineInterval(i.getX1(), i.getX2(), line));
			});
		});
		return res;
	}

	@Override
	public LinearIntervals div(LinearIntervals intervals) {
		Pair<LinearIntervals, LinearIntervals> pair = normalize(this.addZeroes(), intervals);
		LinearIntervalsImpl res = empty();
		biForEach(pair).forEach((f,g) -> {
			
			double x1 = f.getX1();
			double x2 = f.getX2();
			
			double gLowerMin = Math.min(g.getLower().at(x1), g.getLower().at(x2));
			double fLowerMin = Math.min(f.getLower().at(x1), f.getLower().at(x2));
			double gUpperMax = Math.max(g.getUpper().at(x1), g.getUpper().at(x2));
			double fUpperMax = Math.max(f.getUpper().at(x1), f.getUpper().at(x2));
			boolean debug = true;
			
			if (0 <= fLowerMin && 0 < gLowerMin) {
				print(debug, "11");
				res.addAllLower(combineDiv(f.getLower(), g.getUpper(), x1, x2, x -> x.getLower()));
				res.addAllUpper(combineDiv(f.getUpper(), g.getLower(), x1, x2, x -> x.getUpper()));
			}
			if (fUpperMax <= 0 && 0 < gLowerMin) {
				print(debug, "21");
				res.addAllLower(combineDiv(f.getLower(), g.getLower(), x1, x2, x -> x.getLower()));
				res.addAllUpper(combineDiv(f.getUpper(), g.getUpper(), x1, x2, x -> x.getUpper()));
			}
			if ((fUpperMax > 0 && 0 > fLowerMin) && 0 < gLowerMin) {
				print(debug, "31");
				res.addAllLower(combineDiv(f.getLower(), g.getLower(), x1, x2, x -> x.getLower()));
				res.addAllUpper(combineDiv(f.getUpper(), g.getLower(), x1, x2, x -> x.getUpper()));
			}

			if (0 <= fLowerMin && 0 >= gUpperMax) {
				print(debug, "12");
				res.addAllLower(combineDiv(f.getUpper(), g.getUpper(), x1, x2, x -> x.getLower()));
				res.addAllUpper(combineDiv(f.getLower(), g.getLower(), x1, x2, x -> x.getUpper()));
			}
			if (fUpperMax <= 0 && 0 >= gUpperMax) {
				print(debug, "22");
				res.addAllLower(combineDiv(f.getUpper(), g.getLower(), x1, x2, x -> x.getLower()));
				res.addAllUpper(combineDiv(f.getLower(), g.getUpper(), x1, x2, x -> x.getUpper()));
			}
			if ((fUpperMax > 0 && 0 > fLowerMin) && 0 >= gUpperMax) {
				print(debug, "23");
				res.addAllLower(combineDiv(f.getUpper(), g.getUpper(), x1, x2, x -> x.getLower()));
				res.addAllUpper(combineDiv(f.getLower(), g.getUpper(), x1, x2, x -> x.getUpper()));
			}
			
		});
		return res;
	}

	private Collection<LineInterval> combineDiv(Line l1, Line l2, double x1, double x2, Function<LinearIntervals, Collection<LineInterval>> lu) {
		if (equals(l2.getK(), 0.0)) {
			return list(new LineInterval(x1, x2, l1.unoFunc(x -> x / l2.getM())));
		}
		final Function<Double, Double> func = x -> l1.at(x) / l2.at(x);
		final Function<Double, Double> funcd = x -> 
			- l2.getK() * l1.at(x) / l2.at(x) / l2.at(x) + l1.getK() / l2.at(x);
		
		return lu.apply(LinearIntervalsBuilder.build(
				list(x1, x2),
				list(1.0),
				func,
				funcd
			));
	}
	
}
