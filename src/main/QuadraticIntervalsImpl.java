package main;

import java.util.ArrayList;
import java.util.List;

import model.Interval;
import model.QuadraticInterval;
import utils.Utils;

public class QuadraticIntervalsImpl extends Utils implements QuadraticIntervals {
	
	private Interval interval;

	private List<QuadraticInterval> lower = new ArrayList<>();
	
	private List<QuadraticInterval> upper = new ArrayList<>();
	
	public QuadraticIntervalsImpl(Interval interval) {
		this.interval = interval;
	}

	public List<QuadraticInterval> getLower() {
		return lower;
	}

	public List<QuadraticInterval> getUpper() {
		return upper;
	}

	@Override
	public Interval getInterval() {
		return interval;
	}

	
}
