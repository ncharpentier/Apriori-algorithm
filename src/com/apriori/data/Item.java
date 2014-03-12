package com.apriori.data;

import java.util.HashMap;
import java.util.Map;

/* Item class is a fly-weight class */
public class Item {
	
	/* --- Attributes --- */
	/* Static */
	private static Map<String, Item> ITEMS;

	/* No-Static */
	private String key;
	/* --- /Attributes --- */
	
	
	
	/* --- Constructors --- */
	/* Private constructor */
	private Item(String key) { this.key = key; }
	/* --- /Constructors --- */
	
	
	
	/* --- Getter --- */
	public String getKey() { return key; }
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
		} else { // If the item doesn't exists, add it to the static map 
			result = new Item(sKey);
			ITEMS.put(sKey, result);
		}
		return result;
	} /* End getItemByValue */
}
