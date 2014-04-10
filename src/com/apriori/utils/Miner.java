package com.apriori.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.apriori.data.Item;
import com.apriori.data.ItemSet;
import com.apriori.data.Transaction;

public class Miner {

	/* --- Attributes --- */
	/* Static */
	private static String SPLITTER = "\t";

	/* No-Static */
	private List<Transaction> transactions;
	private Map<Integer, List<ItemSet>> itemSets;
	private double minSupport; // support % ( between 1 and 100 )
	private double minSupp; // support double
	/* --- /Attributes --- */



	/* --- Constructors --- */
	public Miner (double minSupport) {
		transactions = new ArrayList<Transaction>();
		itemSets = new HashMap<Integer, List<ItemSet>>();
		this.minSupport = minSupport;
	}
	/* --- /Constructors --- */



	/* Process method */
	public void processFile(String fileToRead) {
		/* First step, parse the file */
		parseFile(fileToRead);

		/* Each transaction is now up */
		System.out.println("Done. " + transactions.size() + " transactions loaded.");

		/* Calc the minSupp compare to the size of transactions */
		minSupp = (double) transactions.size()*((double)minSupport/100);

		/* Calc the frequent k-itemsets */
		init1ItemSets();

		
		for (Item i : Item.ITEMS.values())
			System.out.println("\t" + i);
		
		calcItemSets();

		/* DEBUG DISPLAY */
//		for (Item s : Item.ITEMS.values()) {
//			System.out.println(s.getKey() + " : supp (" + s.getSupp() + ")");
//		}

		for (Integer i : itemSets.keySet()) {
			System.out.println(" ["+i+"] itemsets : " + itemSets.get(i).size());
			for (ItemSet is : itemSets.get(i))
				System.out.println("\t" + is);
		}
		
	} /* End processFile */

	/* initialize the frequent 1-itemsets */
	public void init1ItemSets() {
		// Create the first list
		ArrayList<ItemSet> itemSetsFound = new ArrayList<ItemSet>();

		for (Item i : Item.ITEMS.values())
			if((double)i.getSupp() > minSupp) {
				ArrayList<Item> items = new ArrayList<Item>();
				items.add(i);
				itemSetsFound.add(new ItemSet(1, i.getSupp(), items));	
			}

		itemSets.put(1, itemSetsFound);
		System.out.println("size 1item: " + itemSetsFound.size());
	}

	/* Calc the frequent k-itemsets */
	public void calcItemSets() {
		List<ItemSet> itemSetsFound;

		int k = 2;

		do {
			System.out.println(k);
			itemSetsFound = new ArrayList<ItemSet>();
			/* Calc the candidates to be frequent k itemset, with pruning */
			List<ItemSet> itemSetsCandidates = calcCandidates(k-1);
			System.out.println("k : " + k + " candidSize " + itemSetsCandidates.size());
			/* Calc the support for each candidate */
			for (ItemSet is : itemSetsCandidates) {
				int s = calcSupport(is);

				if(s > minSupp) {
					is.setSupport(s);
					itemSetsFound.add(is);
				}
			}
			// End
			if (!itemSetsFound.isEmpty()) {
				itemSets.put(k, itemSetsFound);
			}
			System.out.println("k : " + k + " candidSize retenu " + itemSetsFound.size());
			k++;
			
		} while (!itemSetsFound.isEmpty());
		
		
	}
	

	/* Make the candidates itemsets */
	public List<ItemSet> calcCandidates(int kinf) {
		System.out.println("Kinf" + kinf);
		List<ItemSet> candidates = new ArrayList<ItemSet>();

		System.out.println("size " + itemSets.get(kinf).size());
		
		for (int i = 0 ; i < (itemSets.get(kinf).size() - 1); i++) {
			
			for (int j = (i + 1) ; j < itemSets.get(kinf).size() ; j++) {
				/* Add the itemset only if only the last items of the 2 itemsets differs */
				List<Item> set = union(itemSets.get(kinf).get(i).getItems(), itemSets.get(kinf).get(j).getItems());
				
				if (set.size() == (kinf + 1)) {
					if (pruneAuthorized(set)) {
						ItemSet tmpIS = new ItemSet(kinf + 1);
						
						tmpIS.addItems(set);

						if (!candidates.contains(tmpIS))
							candidates.add(tmpIS);
					}
				}
			}
		}
		
		return candidates;
	}
	
	private List<Item> union(List<Item> items, List<Item> items2) {
		List<Item> res = new ArrayList<Item>(items);

		res.removeAll(items2);
		res.addAll(items2);
		
		return res;
	}



	/* Check all possible k itemset derived from the k+1 itemset are frequent */
	private boolean pruneAuthorized(List<Item> set) {
		boolean result = true;
		
		for (int i = 0; i < set.size() && result; i++) {
			List<Item> tmpList = new ArrayList<Item>(set);
			/* List - 1 */
			tmpList.remove(i);
			boolean inter = false;
			for (int j = 0 ; j < itemSets.get((set.size() - 1)).size() && !inter ; j++) {
				inter = itemSets.get(set.size() - 1).get(j).getItems().containsAll(tmpList);
			}
			result = inter;
		}
		
		return result;
	}

	/* Calc the support of the given itemset */
	public Integer calcSupport(ItemSet is) {
		Integer result = 0;

		for (Transaction t : transactions) {
			if (t.getItems().containsAll(is.getItems())) {
				result++;
			}
		}

		return result;
	}

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
