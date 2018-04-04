package main;

public class LineInterval {
	
	private double x1;
	
	private double x2;
	
	private Line line;
	
	public LineInterval(double x1, double x2, Line line) {
		this.x1 = x1;
		this.x2 = x2;
		this.line = line;
	}

	public LineInterval withX(double x1, double x2) {
		return new LineInterval(x1, x2, line.clone());
	}

	public double getX1() {
		return x1;
	}
	
	public double getX2() {
		return x2;
	}

	public Line getLine() {
		return line;
	}

	@Override
	public String toString() {
		return "\n" + x1 + ", " + line;
	}

	public Point getIntersectZero() {
		if (sign(line) < 0) {
			return line.intersect(new Line(0, 0));
		}
		return null;
	}

	private double sign(Line line) {
		return line.at(x1) * line.at(x2);
	}
	
}
