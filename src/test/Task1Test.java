package test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import main.Intervals;
import main.task1.Func;
import main.task1.Sample;
import main.task1.Samples;

public class Task1Test {
	
	
	@Test
	public void test() {
		Sample s = Samples.getSample1();
		Intervals is = Func.func(s.sp, s.spozn, s.y, s.yd);
		System.out.println(is);
	}

	

}
