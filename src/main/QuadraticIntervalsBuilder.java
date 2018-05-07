package main;

import java.util.List;

import model.Point;
import utils.Utils;

public class QuadraticIntervalsBuilder extends Utils {

	public static QuadraticIntervals build(LinearIntervals intervals, List<Point> points) {
		
		QuadraticIntervals res = new QuadraticIntervalsImpl(intervals.getInterval());
		print(intervals.intervals().size());
		print(points);
		return res;
	}

}
