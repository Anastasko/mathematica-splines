package test;

import org.junit.Assert;
import org.junit.Test;

import main.func_store.AbstractFunction;
import main.func_store.FunctionStore;
import utils.Utils;

public class GetSpoznTest extends Utils {

	@Test
	public void test() {
		AbstractFunction x3 = new FunctionStore(-1,1).x3();
		Assert.assertEquals(3, x3.getPoints().size());
		Assert.assertArrayEquals(new Double[] {-1.0, -1.0, 1.0, 1.0, 1.0}, 
				x3.getSpozn(list(-1.0,-0.5,0.0,0.3,0.5,1.0)).toArray());
	}
	
}
