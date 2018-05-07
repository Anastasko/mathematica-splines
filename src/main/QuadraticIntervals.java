package main;

import java.util.List;

import model.Interval;
import model.QuadraticInterval;

public interface QuadraticIntervals {

	List<QuadraticInterval> getLower();

	List<QuadraticInterval> getUpper();

//	List<QuadraticsInterval> intervals();

	Interval getInterval();
}
