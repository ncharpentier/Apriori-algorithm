package com.apriori.data;

import java.util.ArrayList;
import java.util.List;

public class Transaction {
	private List<Item> items;
	
	public Transaction() {
		items = new ArrayList<Item>();
	}
	
	public void addItem(Item item) {
		items.add(item);
	}

	public List<Item> getItems() {
		return items;
	}
	
	
}
