package utils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import main.Intervals;
import main.LineInterval;
import main.MyException;

public class Utils extends Compare {
	
	@SafeVarargs
	public static <T> List<T> list(T ...ts) {
		return Arrays.asList(ts);
	}
	
	public static void lowerUpper(Consumer<Function<Intervals, List<LineInterval>>> consumer) {
		consumer.accept(x -> x.getLower());
		consumer.accept(x -> x.getUpper());
	}
	
	public static void print(Object o) {
		System.out.println(o);
	}

	public static MyException fail(String... strings) {
		String concat = "";
		for(int i = strings.length - 1; i >=0; --i) {
			concat += strings[i] + "\n";
		}
		return new MyException(concat);
	}

}
