package com.apriori;

import com.apriori.utils.Miner;

public class Launcher {

	public static String FILE_TO_READ = "Resources/structured/tickets_de_caisse.txt";
	
	public static void main(String[] args) {
		new Miner().processFile(FILE_TO_READ);
	}

}
