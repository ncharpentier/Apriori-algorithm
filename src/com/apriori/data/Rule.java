package com.apriori.data;

import java.util.ArrayList;
import java.util.List;

public class Rule {
	/* --- Attributes --- */
	private double confidence;
	private List<Item> hypothesis;
	private Item consequence;
	/* --- /Attributes --- */

	/* --- Constructors --- */
	public Rule(List<Item> hyp, Item cons) {
		hypothesis = new ArrayList<Item>(hyp);
		consequence = cons;
	}
	
	public Rule(List<Item> hyp, Item cons, double conf) {
		hypothesis = new ArrayList<Item>(hyp);
		consequence = cons;
		confidence = conf;
	}
	/* --- /Constructors --- */
	
	
	/* --- Getters & Setters --- */
	public List<Item> getHypothesis() { return hypothesis; }
	public Item getConsequence() { return consequence; }
	public double getConfidence() { return confidence; }
	public void setConfidence(double conf) { confidence = conf; }
	/* --- /Getter & Setters --- */

	@Override
	public String toString() {
		return "Rule {" + hypothesis + "==>" + consequence + ", confidence = " + confidence + "}";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(confidence);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((consequence == null) ? 0 : consequence.hashCode());
		result = prime * result
				+ ((hypothesis == null) ? 0 : hypothesis.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rule other = (Rule) obj;
		if (Double.doubleToLongBits(confidence) != Double
				.doubleToLongBits(other.confidence))
			return false;
		if (consequence == null) {
			if (other.consequence != null)
				return false;
		} else if (!consequence.equals(other.consequence))
			return false;
		if (hypothesis == null) {
			if (other.hypothesis != null)
				return false;
		} else if (!hypothesis.equals(other.hypothesis))
			return false;
		return true;
	}
	
	
	
}
