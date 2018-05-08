package main.func_store;

import java.util.List;

import org.junit.Test;

import utils.Utils;

public class CosFunctionFactory extends Utils {

	@Test
	public void test() {
		AbstractFunction cos = create(0, 6);
		print(cos.getSp());
		print(cos.getSpozn());
//		Assert.assertArrayEquals(
//				list(2.0, Const.Pi/2, 4.71238898038469, 6.0).toArray(), 
//				sin.getSp().toArray());
//		
//		Assert.assertArrayEquals(
//				list(-1.0, 1.0, 1.0).toArray(), 
//				sin.getSpozn().toArray());
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
		return new CosFunction(
			sp,
			spozn
		);
	}
	
	private static double pointByNumber(Long pointNumber) {
		return pointNumber * Pi / 2;
	}
	
	private static double oznByNumber(Long pointNumber) {
		return (pointNumber + 1) / 2 % 2 == 0 ? -1.0 : 1.0;
	}

}
