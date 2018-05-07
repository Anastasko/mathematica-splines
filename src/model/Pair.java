package model;

public class Pair<F, S> {
	
	private F one;
	
	private S two;
	
	public Pair(F one, S two) {
		this.one = one;
		this.two = two;
	}
	
	public F getOne() {
		return one;
	}
	
	public S getTwo() {
		return two;
	}

}
