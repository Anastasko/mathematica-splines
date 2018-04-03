package main;

public class LineInterval {
	
	private double x;
	
	private Line upper;
	
	private Line lower;
	
	public LineInterval(double x, Line lower, Line upper) {
		this.x = x;
		this.lower = lower;
		this.upper = upper;
	}

	public LineInterval setX(double x) {
		return new LineInterval(x, lower.clone(), upper.clone());
	}

	public double getX() {
		return x;
	}

	public Line getUpper() {
		return upper;
	}

	public Line getLower() {
		return lower;
	}
	
	@Override
	public String toString() {
		return "\nx=" + x + ", \nupper=" + upper + ", \nlower=" + lower + "\n";
	}
	
}
