package com.apriori.data;

import java.util.ArrayList;
import java.util.List;

public class ItemSet {
	
	private int k;
	private int support;
	private List<Item> items;

	
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
	
	public int getK() {
		return k;
	}
	
	public int getSupport() {
		return support;
	}

	public void setSupport(int support) {
		this.support = support;
	}

	public List<Item> getItems() {
		return items;
	}

	public void addItem(Item item) {
		items.add(item);
	}
}
