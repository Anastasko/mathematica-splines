package model;

public class QuadraticInterval extends Interval {
	
	private Parabola parabola;

	public QuadraticInterval(double x1, double x2, Parabola parabola) {
		super(x1, x2);
		this.parabola = parabola;
	}

	public QuadraticInterval withX(double x1, double x2) {
		return new QuadraticInterval(x1, x2, parabola.clone());
	}
	
	public Parabola getParabola() {
		return parabola;
	}

	@Override
	public String toString() {
		return "\n[" + getX1() + ", " + getX2() + "] " + ", " + parabola;
	}

	public QuadraticInterval withX2(double x2) {
		return withX(getX1(), x2);
	}
	
}
