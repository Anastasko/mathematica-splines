package main;

public class Line {
	
	private double k;
	
	private double m;

	public Line(double k, double m) {
		this.k = k;
		this.m = m;
	}
	
	public static Line make(Point a, Point b) {
		double k = (b.getY() - a.getY()) / (b.getX() - a.getX());
		double m = a.getY() - k * a.getX();
		return new Line(k, m);
	}
	
	public Point intersect(Line l) {
		if (l.k == k && l.m != m) {
			throw new MyException("Lines Intersection: k1 == k2 && m1 != m2");
		}
		double xc = -(l.m - m);
		if (xc != 0) xc /= (l.k - k);
		double yc = k*xc + m;
		return new Point(xc, yc);
	}

	public double getK() {
		return k;
	}

	public double getM() {
		return m;
	}
	
	public Line clone() {
		return new Line(k, m);
	}

	@Override
	public String toString() {
		return "{" + k + ", " + m + "}";
	}
	

}
