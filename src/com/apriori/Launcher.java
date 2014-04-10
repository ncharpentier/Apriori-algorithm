package com.apriori;

import com.apriori.utils.Miner;

public class Launcher {

	public static String FILE_TO_READ = "Resources/unstructured/transa.txt";
	public static double MIN_SUPPORT = 20;
	
	public static void main(String[] args) {
		new Miner(MIN_SUPPORT).processFile(FILE_TO_READ);
	}

}
