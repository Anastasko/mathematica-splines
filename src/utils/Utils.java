package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import main.Intervals;
import main.LineInterval;
import main.MyException;

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
	
	public static void lowerUpper(Consumer<Function<Intervals, List<LineInterval>>> consumer) {
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

}
