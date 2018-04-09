package main.task1;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import main.Const;
import utils.Utils;

public class SinFunctionFactory extends Utils {
	
	@Test
	public void test() {
		AbstractFunction sin = create(2, 6);
		
		Assert.assertArrayEquals(
				list(2.0, 3.141592653589793, 4.71238898038469, 6.0).toArray(), 
				sin.getSp().toArray());
		
		Assert.assertArrayEquals(
				list(-1.0, 1.0, 1.0).toArray(), 
				sin.getSpozn().toArray());
	}
	
	@Test
	public void test2() {
		AbstractFunction sin = create(-Const.Pi, 0);
		
		Assert.assertArrayEquals(
				list(-Const.Pi, -Const.Pi/2, 0.0).toArray(), 
				sin.getSp().toArray());
		Assert.assertArrayEquals(
				list(1.0, 1.0).toArray(), 
				sin.getSpozn().toArray());
	}
	
	@Test
	public void test3() {
		AbstractFunction sin = create(0, Const.Pi);
		Assert.assertArrayEquals(
				list(0.0, Const.Pi/2, Const.Pi).toArray(), 
				sin.getSp().toArray());
		Assert.assertArrayEquals(
				list(-1.0, -1.0).toArray(), 
				sin.getSpozn().toArray());
	}

	public static AbstractFunction create(double lower, double upper) {
		List<Long> list = list();
		Long number = 0L;
		while (less(lower, pointByNumber(number))) list.add(number--);
		number = 1L;
		while (less(pointByNumber(number), upper)) list.add(number++);
		sort(list);
		List<Double> sp = list();
		List<Double> spozn = list();
		Long prev = first(list) - 1;
		sp.add(lower);
		spozn.add(oznByNumber(prev));
		list.forEach(num -> {
			double point = pointByNumber(num);
			if (less(lower, point) && less(point, upper)) {
				sp.add(point);
				spozn.add(oznByNumber(num));
			}
		});
		sp.add(upper);
		return new AbstractFunction(
			sp,
			spozn,
			x -> Math.sin(x),
			x -> Math.cos(x)
		);
	}
	
	private static double pointByNumber(Long pointNumber) {
		return pointNumber * Const.Pi / 2;
	}
	
	private static double oznByNumber(Long pointNumber) {
		if (pointNumber < 0) {
			return -1*oznByNumber(~pointNumber);
		}
		return pointNumber / 2 % 2 == 0 ? -1.0 : 1.0;
	}
	
	
}
