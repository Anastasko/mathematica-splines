package main;

public class LineInterval extends Interval {
	
	private Line line;
	
	public LineInterval(double x1, double x2, Line line) {
		super(x1, x2);
		this.line = line;
	}

	public LineInterval withX(double x1, double x2) {
		return new LineInterval(x1, x2, line.clone());
	}
	
	public Line getLine() {
		return line;
	}

	@Override
	public String toString() {
		return "\n" + getX1() + ", " + line;
	}

	public Point getIntersectZero() {
		if (sign(line) < 0) {
			return line.intersect(new Line(0, 0));
		}
		return null;
	}

	private double sign(Line line) {
		return line.at(getX1()) * line.at(getX2());
	}
	
}
