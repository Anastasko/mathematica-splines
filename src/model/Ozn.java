package model;

import java.util.List;

public class Ozn {
	
	private List<Double> sp;
	private List<Double> spozn;
	
	public Ozn(List<Double> sp, List<Double> spozn) {
		this.sp = sp;
		this.spozn = spozn;
	}
	public List<Double> getSp() {
		return sp;
	}
	public void setSp(List<Double> sp) {
		this.sp = sp;
	}
	public List<Double> getSpozn() {
		return spozn;
	}
	public void setSpozn(List<Double> spozn) {
		this.spozn = spozn;
	}

}
