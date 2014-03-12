package com.apriori.data;

import java.util.HashMap;
import java.util.Map;

/* item class is a fly-weight class */
public class Item {
	public static Map<String, Item> ITEMS;
	
	/* add the item to the static list */
	public Item(String lib, String value){
		if (ITEMS == null)
			ITEMS = new HashMap<String, Item>();
		
		String constructor = construct(lib, value);
		
		if (!ITEMS.containsKey(constructor))
			ITEMS.put(constructor, this);
	}
	

	/* Static method to uniform the string built from the lib & value */
	private static String construct(String lib, String value) {
		return lib + "=" + value;
	}
	
	/* Static method to get the item from the static list thanks to the lib & value */
	public static Item getItemByValue(String lib, String value) {
		Item result = null;
		
		String constructor = construct(lib, value);
		
		if (ITEMS == null)
			ITEMS = new HashMap<String, Item>();
		
		if (ITEMS.containsKey(constructor))
			result = ITEMS.get(constructor);
		
		return result;
	}
}
