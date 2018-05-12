package test;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import utils.Utils;

public class UtilsTest extends Utils {

	@Test
	public void union() {
		List<Double> l1 = list(1.0, 2.0, 5.0);
		List<Double> l2 = list(1.0, 3.0, 5.0, 7.0);
		List<Double> res = Utils.union(l1, l2);
		print(res);
		Assert.assertArrayEquals(new Double[] {1.0, 2.0, 3.0, 5.0, 7.0}, res.toArray());
	}
	
}
