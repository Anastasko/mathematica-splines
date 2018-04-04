package main;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class Utils {
	
	@SafeVarargs
	public static <T> List<T> list(T ...ts) {
		return Arrays.asList(ts);
	}
	
	public static void lowerUpper(Consumer<Function<Intervals, List<LineInterval>>> consumer) {
		consumer.accept(x -> x.getLower());
		consumer.accept(x -> x.getUpper());
	}

}
