package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import main.LinearIntervals;
import model.LineInterval;
import validation.MyException;
import validation.Validator;

public class Utils extends Compare {
	
	@SafeVarargs
	public static <T> List<T> list(T ...ts) {
		List<T> l = new ArrayList<>();
		l.addAll(Arrays.asList(ts));
		return l;
	}
	
	public static <T> T last(List<T> l) {
		return l.get(l.size() - 1);
	}
	
	public static <T> T first(List<T> l) {
		return l.get(0);
	}
	
	public static <T> void reverse(List<T> list) {
		Collections.reverse(list);
	}
	
	public static <T extends Comparable<T>> void sort(List<T> list) {
		Collections.sort(list);
	}
	
	public static void lowerUpper(Consumer<Function<LinearIntervals, List<LineInterval>>> consumer) {
		consumer.accept(x -> x.getLower());
		consumer.accept(x -> x.getUpper());
	}
	
	public static void print(Object o) {
		System.out.println(o);
	}
	
	public static void printErr(Object s) {
		System.err.println(s);
	}
	
	public static void print(boolean output, Object o) {
		if (output) print(o);
	}

	public static void fail(Object... strings) {
		String concat = "";
		for(int i = strings.length - 1; i >=0; --i) {
			concat += "\n" + strings[i];
		}
		throw new MyException(concat);
	}
	
	public static List<Double> union(List<Double> a, List<Double> b) {
		List<Double> res = new ArrayList<>();
		Validator.checkAsc(a);
		Validator.checkAsc(b);
		int i=0;
		int j=0;
		int n=a.size();
		int m=b.size();
		while (i < n || j < m) {
			if (i == n) {
				if (lessCheck(res, b.get(j))) res.add(b.get(j));
				++j;
			} else if (j == m) {
				if (lessCheck(res, a.get(i))) res.add(a.get(i));
				++i;
			} else {
				if (equals(a.get(i), b.get(j))) {
					if (lessCheck(res, a.get(i))) {
						res.add(a.get(i));
					}
					++i;
					++j;
				} else if (less(a.get(i), b.get(j))) {
					if (lessCheck(res, a.get(i))) res.add(a.get(i));
					++i;
				} else {
					if (lessCheck(res, a.get(i))) res.add(b.get(j));
					++j;
				}
			}
		}
		return res;
	}

	private static boolean lessCheck(List<Double> list, Double d) {
		if (list.isEmpty()) return true;
		return less(last(list), d);
	}

}
