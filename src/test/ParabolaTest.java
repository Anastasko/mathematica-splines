package test;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import model.Parabola;
import model.Point;
import utils.Utils;

public class ParabolaTest extends Utils {

	@Test
	public void testCross() {
		Parabola a = new Parabola(1,0,0);
		Parabola b = new Parabola(-1,0,2);
		List<Point> p = a.cross(b);
		print(p);
		Assert.assertEquals(2, p.size());
		Assert.assertEquals(-1, p.get(0).getX(), EPS);
		Assert.assertEquals(1, p.get(1).getX(), EPS);
		Assert.assertEquals(1, p.get(0).getY(), EPS);
		Assert.assertEquals(1, p.get(1).getY(), EPS);
	}
	
}
