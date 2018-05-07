package main;

import java.util.List;

import model.Interval;
import model.LineInterval;
import model.LinesInterval;

public interface LinearIntervals {

	List<LineInterval> getLower();

	List<LineInterval> getUpper();

	LinearIntervals plus(LinearIntervals intervals);

	LinearIntervals minus(LinearIntervals intervals);

	LinearIntervals plus(double d);
	
	LinearIntervals div(double x);

	LinearIntervals mult(double x);

	LinearIntervals mult(LinearIntervals intervals);

	LinearIntervals div(LinearIntervals intervals);

	List<LinesInterval> intervals();

	Interval getInterval();

	LinearIntervals addZeroes();

}
