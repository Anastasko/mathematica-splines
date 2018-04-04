package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import main.Intervals;
import main.MyException;
import main.task1.Func;
import main.task1.Sample;
import main.task1.Samples;

public class Task1Test {
	
	@Test
	public void testSample1() {
		Sample s = Samples.getSample1();
		Intervals is = func(s);
		output("-sin", is);
	}

	@Test
	public void testSample2() {
		Sample s = Samples.getSample2();
		Intervals is = func(s);
		output("x3", is);
	}
	
	@Test
	public void testSamplePlus() {
		Sample s1 = Samples.getSample1();
		Sample s2 = Samples.getSample2();
		Intervals is1 = func(s1);
		Intervals is2 = func(s2);
		output("x^3 div 20 - 5sin", is1.mult(5).plus(is2.div(20)));
	}
	
	@Test
	public void testSampleTimes() {
		Sample s1 = Samples.getSample1();
		Sample s2 = Samples.getSample2();
		Intervals is1 = func(s1);
		Intervals is2 = func(s2);
		output("x^3 times -sin", is1.mult(is2));
	}

	private void output(String name, Intervals is) {
		
		String fileName = "/Users/lorry/data/" + name;
		try {
			
			File file = new File(fileName+"-lower.txt");
			PrintWriter pw1 = new PrintWriter(file);
			
			is.forEach(i -> {
				pw1.println(i.getX1() + " " + i.getLower().getK() + " " + i.getLower().getM());
			});
			pw1.println(is.last().getX2() + " " + is.last().getLower().getK() + " " + is.last().getLower().getM());
			pw1.close();
		
			file = new File(fileName+"-upper.txt");
			PrintWriter pw2 = new PrintWriter(file);
			is.forEach(i -> {
				pw2.println(i.getX1() + " " + i.getUpper().getK() + " " + i.getUpper().getM());
			});
			pw2.println(is.last().getX2() + " " + is.last().getLower().getK() + " " + is.last().getLower().getM());
			pw2.close();
			
		} catch (FileNotFoundException e) {
			throw new MyException(e);
		}
	}

	private Intervals func(Sample s) {
		return Func.func(s.sp, s.spozn, s.y, s.yd);
	}

}
