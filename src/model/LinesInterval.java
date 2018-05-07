package model;

public class LinesInterval extends Interval {
	
	private Line lower;
	
	private Line upper;

	public LinesInterval(double x1, double x2, Line lower, Line upper) {
		super(x1, x2);
		this.lower = lower;
		this.upper = upper;
	}

	public Line getLower() {
		return lower;
	}

	public Line getUpper() {
		return upper;
	}
	
	public double getLowerAt(double x) {
		return lower.at(x);
	}

	public double getUpperAt(double x) {
		return upper.at(x);
	}
	
}
