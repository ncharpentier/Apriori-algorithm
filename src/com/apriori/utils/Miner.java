package com.apriori.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.apriori.data.ItemSet;
import com.apriori.data.Transaction;

public class Miner {
	
	/* --- Attributes --- */
	/* Static */
	public static String SPLITTER = "\t";
	
	/* No-Static */
	public List<Transaction> transactions;
	public Map<Integer, List<ItemSet>> itemSets;
	/* --- /Attributes --- */
	
	
	
	/* --- Constructors --- */
	public Miner () {
		transactions = new ArrayList<Transaction>();
		itemSets = new HashMap<Integer, List<ItemSet>>();
	}
	/* --- /Constructors --- */
	
	
	
	/* Process method */
	public void processFile(String fileToRead) {
		/* First step, parse the file */
		parseFile(fileToRead);
		
		/* Each transaction is now up */
		System.out.println("Done. " + transactions.size() + " transactions loaded.");
	} /* End processFile */
	
	
	/* Call the TransactionParser for each line */
	public void parseFile(String fileToRead) {
		TransactionParser tParser = null;
		BufferedReader bufferReader = null;

		try {
			String currLine;
			bufferReader = new BufferedReader(new FileReader(fileToRead));

			// Get the first line
			if ((currLine = bufferReader.readLine()) != null) {				
				tParser = new TransactionParser(currLine.split(SPLITTER));					
			}
			
			// Parse all lines
			while ((currLine = bufferReader.readLine()) != null) {
				try { // File could be corrupted, it means that the current line got more tokens than the first line
					transactions.add(tParser.parseLine(currLine, SPLITTER));
				} catch (DataLengthNotConcordException e) {
					e.printStackTrace();
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferReader != null)
					bufferReader.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	} /* End parseFile */
}
