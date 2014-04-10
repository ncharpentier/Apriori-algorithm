package com.apriori;

import com.apriori.utils.Miner;

public class Launcher {

	public static String FILE_TO_READ = "Resources/unstructured/transa.txt";
	public static int MIN_SUPPORT = 2;
	
	public static void main(String[] args) {
		new Miner(MIN_SUPPORT).processFile(FILE_TO_READ);
	}

}
