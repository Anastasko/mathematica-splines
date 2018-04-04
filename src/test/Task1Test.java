package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import org.junit.Test;

import main.Intervals;
import main.LineInterval;
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

//	@Test
	public void testSampleTimes() {
		Sample s1 = Samples.getSample1();
		Sample s2 = Samples.getSample2();
		Intervals is1 = func(s1);
		Intervals is2 = func(s2);
		output("x^3 times -sin", is1.mult(is2));
	}
	
	private static final String filePath = "/Users/lorry/data/";

	private void output(String name, Intervals intervals) {
		write(intervals.getLower(), filePath + name + "-lower");
		write(intervals.getUpper(), filePath + name + "-upper");
		System.out.println(name);
	}

	private void write(List<LineInterval> list, String fileName) {
		File file = new File(fileName + ".txt");
		try {
			PrintWriter pw = new PrintWriter(file);
			list.forEach(i -> pw.println(i.getX1() + " " + i.getLine().getK() + " " + i.getLine().getM()));
			LineInterval last = list.get(list.size()-1);
			pw.println(last.getX2() + " " + last.getLine().getK() + " " + last.getLine().getM());
			pw.close();
		} catch (FileNotFoundException e) {
			throw new MyException(e);
		}
	}

	private Intervals func(Sample s) {
		return Func.func(s.sp, s.spozn, s.y, s.yd);
	}

}
