package test;

import org.junit.Assert;
import org.junit.Test;

import main.Const;
import model.Line;
import model.Point;

public class LineTest {

	@Test
	public void testMake() {
		Point a = new Point(0,1);
		Point b = new Point(1,0);
		Line line = Line.make(a, b);
		Assert.assertEquals(-1, line.getK(), Const.EPS);
		Assert.assertEquals(1, line.getM(), Const.EPS);
	}

	@Test
	public void testIntersect() {
		Line l1 = new Line(2,0);
		Line l2 = new Line(-2,2);
		Point p = l1.intersect(l2);
		Assert.assertEquals(0.5, p.getX(), Const.EPS);
		Assert.assertEquals(1, p.getY(), Const.EPS);
	}
	
}
