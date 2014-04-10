package com.apriori.data;

import java.util.ArrayList;
import java.util.List;

public class ItemSet {
	
	/* --- Attributes --- */
	private int k;
	private int support;
	private List<Item> items;
	/* --- /Attributes --- */

	
	
	/* --- Constructors --- */
	public ItemSet(int k) {
		super();
		this.k = k;
		items = new ArrayList<Item>();
	}

	public ItemSet(int k, int support, List<Item> items) {
		super();
		this.k = k;
		this.support = support;
		this.items = items;
	}
	/* --- /Constructors --- */
	
	
	
	/* --- Getters & Setters --- */
	public int getK() { return k; }
	
	public int getSupport() { return support; }
	public void setSupport(int support) { this.support = support; }

	public List<Item> getItems() { return items; }
	/* --- /Getter & Setters --- */
	
	
	
	
	/* Add an item to the list */
	public void addItem(Item item) {
		items.add(item);
	} /* End addItem */
	@Override
	public String toString() {
		return "ItemSet [k=" + k + ", support=" + support + ", items=" + items
				+ "]";
	}

	/* Add items to the list */
	public void addItems(List<Item> items) {
		this.items.addAll(items);
	} /* End addItems */
}
