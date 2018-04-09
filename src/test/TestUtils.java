package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import main.Interval;
import main.Intervals;
import main.LineInterval;
import main.MyException;
import main.task1.Func;
import main.task1.AbstractFunction;
import utils.Utils;
import validation.Validator;

public class TestUtils extends Utils {
	
	private static final String filePath = "/Users/lorry/data/";

	protected void output(String name, Intervals intervals) {
		MyException e = Validator.validate(intervals);
		if (e != null) {
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

	protected Intervals func(AbstractFunction s) {
		return Func.func(s.getSp(), s.getSpozn(), s.getY(), s.getYd());
	}

}
