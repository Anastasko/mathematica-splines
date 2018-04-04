package main;

import java.util.ArrayList;
import java.util.List;

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

	public LineInterval withX(double x1, double x2) {
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

	public Point getUpperIntersectZero() {
		return getIntersectZero(upper);
	}
	
	public Point getLowerIntersectZero() {
		return getIntersectZero(lower);
	}

	private Point getIntersectZero(Line line) {
		if (sign(line) < 0) {
			return line.intersect(new Line(0, 0));
		}
		return null;
	}

	private double sign(Line line) {
		return line.at(x1) * line.at(x2);
	}
	
	public static List<LineInterval> mult(LineInterval a, LineInterval b) {
		List<LineInterval> res = new ArrayList<>();
		
		return res;
	}
	
}
