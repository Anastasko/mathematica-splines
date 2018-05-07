package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import main.LinearIntervals;
import main.LinearIntervalsBuilder;
import main.QuadraticIntervals;
import main.QuadraticIntervalsBuilder;
import main.func_store.AbstractFunction;
import model.Interval;
import model.LineInterval;
import model.Point;
import model.QuadraticInterval;
import utils.Utils;
import validation.MyException;
import validation.Validator;

public class TestUtils extends Utils {
	
	private static final String filePath = "/Users/lorry/data/";

	protected void output(String name, LinearIntervals intervals) {
		try {
			Validator.validate(intervals);
		} catch (MyException e) {
			print(intervals);
			printErr(name + " failed!");
			throw e;
		}
		write(segments(intervals.getLower()), name + "-lower");
		write(segments(intervals.getUpper()), name + "-upper");
		write(bounds(intervals.getInterval()), name + "-bounds");
		print(name + " OK!");
	}
	
	protected void output(String name, QuadraticIntervals q) {
//		try {
//			Validator.validate(intervals);
//		} catch (MyException e) {
//			print(intervals);
//			printErr(name + " failed!");
//			throw e;
//		}
		write(segments2(q.getLower()), name + "-lower");
		write(segments2(q.getUpper()), name + "-upper");
		write(bounds(q.getInterval()), name + "-bounds");
		print(name + " OK!");
	}

	private String bounds(Interval interval) {
		return interval.getX1() + " " + interval.getX2();
	}
	
	private String segments2(List<QuadraticInterval> list) {
		StringBuilder sb = new StringBuilder();
		list.forEach(i -> sb.append(i.getX1() + " " + i.getParabola().getA() + " " + i.getParabola().getB() + " " + i.getParabola().getC()  + "\n"));
		sb.append(last(list).getX2() + " " + last(list).getParabola().getA() + " " + last(list).getParabola().getB()+ " " + last(list).getParabola().getC());
		return sb.toString();
	}

	private String segments(List<LineInterval> list) {
		StringBuilder sb = new StringBuilder();
		list.forEach(i -> sb.append(i.getX1() + " " + i.getLine().getK() + " " + i.getLine().getM() + "\n"));
		sb.append(last(list).getX2() + " " + last(list).getLine().getK() + " " + last(list).getLine().getM());
		return sb.toString();
	}

	private void write(String text, String fileName) {
		File file = new File(filePath + fileName + ".txt");
		try {
			PrintWriter pw = new PrintWriter(file);
			pw.println(text);
			pw.close();
		} catch (FileNotFoundException e) {
			throw new MyException(e);
		}
	}

	protected LinearIntervals build(AbstractFunction s) {
		return LinearIntervalsBuilder.build(s.getSp(), s.getSpozn(), s.getY(), s.getYd());
	}
	
	protected QuadraticIntervals build(LinearIntervals i, List<Point> points) {
		return QuadraticIntervalsBuilder.build(i, points);
	}

}
