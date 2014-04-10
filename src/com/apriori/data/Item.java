package com.apriori.data;

import java.util.HashMap;
import java.util.Map;

/* Item class is a fly-weight class */
public class Item {
	
	/* --- Attributes --- */
	/* Static */
	public static Map<String, Item> ITEMS;

	/* No-Static */
	private String key;
	private Integer supp;
	/* --- /Attributes --- */
	
	
	
	/* --- Constructors --- */
	/* Private constructor */
	private Item(String key) { this.key = key; supp = 1; }
	/* --- /Constructors --- */
	
	
	
	/* --- Getter --- */
	public String getKey() { return key; }
	public Integer getSupp() { return supp; }
	/* --- /Getter --- */
		
	/* Static method to uniform the built string from the lib & value */
	private static String construct(String lib, String value) {
		return lib + "=" + value;
	} /* End construct */
	
	/* Static method to init (or re-init) the map */
	public static void init() {
		ITEMS = new HashMap<String, Item>();
	} /* End init */
	
	/* Static method to get the item from the static list thanks to the lib & value */
	public static Item getItemByValue(String lib, String value) {
		Item result = null;
		String sKey = construct(lib, value);
		
		if (ITEMS == null)
			init();

		if (ITEMS.containsKey(sKey)) {
			result = ITEMS.get(sKey);
			result.supp++;
		} else { // If the item doesn't exists, add it to the static map 
			result = new Item(sKey);
			ITEMS.put(sKey, result);
		}
		return result;
	} /* End getItemByValue */



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
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
		Item other = (Item) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}



	@Override
	public String toString() {
		return "Item [key=" + key + ", supp=" + supp + "]";
	}
	
	
}
