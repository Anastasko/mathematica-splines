package main;

public class LineInterval {
	
	private double x1;
	
	private double x2;
	
	private Line upper;
	
	private Line lower;
	
	public LineInterval(double x1, double x2, Line lower, Line upper) {
		this.x1 = x1;
		this.x2 = x2;
		this.lower = lower;
		this.upper = upper;
	}

	public LineInterval setX(double x1, double x2) {
		return new LineInterval(x1, x2, lower.clone(), upper.clone());
	}

	public double getX1() {
		return x1;
	}
	
	public double getX2() {
		return x2;
	}

	public Line getUpper() {
		return upper;
	}

	public Line getLower() {
		return lower;
	}
	
	@Override
	public String toString() {
		return "\nx=" + x1 + ", \nupper=" + upper + ", \nlower=" + lower + "\n";
	}
	
}
