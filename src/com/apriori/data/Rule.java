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
	public double getConfidence() { return confidence; }
	public void setConfidence(double conf) { confidence = conf; }
	/* --- /Getter & Setters --- */

	@Override
	public String toString() {
		return "Rule [" + hypothesis + "==>" + consequence + ", confidence = " + confidence + "]";
	}
	
	
}
