package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import main.LinearIntervals;
import main.LinearIntervalsBuilder;
import main.func_store.AbstractFunction;
import model.Interval;
import model.LineInterval;
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

	private String bounds(Interval interval) {
		return interval.getX1() + " " + interval.getX2();
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

}
