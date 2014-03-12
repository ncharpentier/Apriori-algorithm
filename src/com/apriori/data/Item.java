package com.apriori.data;

public class Item {
	private int id;
	private String description;
	
	public Item(int id, String des){
		this.id = id;
		this.description = des;
	}
	
	public int getId(){
		return this.id;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public boolean isEqual(Item c) {
		return this.id == c.id;
	}
	}
