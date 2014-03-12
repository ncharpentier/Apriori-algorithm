package com.apriori.data;

import java.util.ArrayList;
import java.util.List;

public class Transaction {
	
	/* --- Attributes --- */
	private List<Item> items; // List of items which compose the transaction
	/* --- /Attributes --- */
	
	/* --- Constructors --- */
	public Transaction() {
		items = new ArrayList<Item>();
	}
	/* --- /Constructors --- */

	/* --- Getter --- */
	public List<Item> getItems() {
		return items;
	}
	/* --- /Getter --- */
	
	/* Add an item to the list */
	public void addItem(Item item) {
		items.add(item);
	} /* End addItem */
}
