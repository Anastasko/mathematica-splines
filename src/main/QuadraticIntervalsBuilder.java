package main;

import utils.Utils;

public class QuadraticIntervalsBuilder extends Utils {

	public static QuadraticIntervals build(LinearIntervals intervals) {
		
		QuadraticIntervals res = new QuadraticIntervalsImpl(intervals.getInterval());
		
		return res;
	}

}
