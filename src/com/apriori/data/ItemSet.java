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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((items == null) ? 0 : items.hashCode());
		result = prime * result + k;
		result = prime * result + support;
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
		ItemSet other = (ItemSet) obj;
		if (items == null) {
			if (other.items != null)
				return false;
		} else {
			boolean itemsEgaux = true;
			for (Item i : items) {
				itemsEgaux &= other.getItems().contains(i);
			}
			if (!itemsEgaux)
				return false;
		}
		if (k != other.k)
			return false;
		if (support != other.support)
			return false;
		return true;
	}
}
