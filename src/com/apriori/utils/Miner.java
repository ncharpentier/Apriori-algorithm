package com.apriori.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.apriori.Launcher.Method;
import com.apriori.data.Item;
import com.apriori.data.ItemSet;
import com.apriori.data.Rule;
import com.apriori.data.Transaction;

public class Miner {

	/* --- Attributes --- */
	/* Static */
	private static String SPLITTER = "\t";

	/* No-Static */
	private List<Rule> rules;
	private List<Transaction> transactions;
	private Map<Integer, List<ItemSet>> itemSets;
	private Map<Integer, List<ItemSet>> closedItemSets;
	private Map<Integer, List<ItemSet>> maximalItemSets;
	private double minSupport; // support % ( between 1 and 100 )
	private double minSupp; // support double
	private double minConf; // min confidence for generated rules
	/* --- /Attributes --- */

	/* --- Constructors --- */
	public Miner (double minSupport, double minConf) {
		transactions = new ArrayList<Transaction>();
		itemSets = new HashMap<Integer, List<ItemSet>>();
		closedItemSets = new HashMap<Integer, List<ItemSet>>();
		maximalItemSets = new HashMap<Integer, List<ItemSet>>();
		rules = new ArrayList<Rule>();
		this.minSupport = minSupport;
		this.minConf = minConf;
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
		calcItemSets();

		/* DEBUG DISPLAY */
		//		for (Item i : Item.ITEMS.values())
		//			System.out.println("\t" + i);
		//		for (Item s : Item.ITEMS.values()) {
		//			System.out.println(s.getKey() + " : supp (" + s.getSupp() + ")");
		//		}

	} /* End processFile */
	
	/* Generate itemSets and rules relate to the method wanted */
	public void genAndDisplayRules(Method method) {
		Map<Integer, List<ItemSet>> itemSetsToGenerate;
		
		if (method == Method.CLOSED) {
			/* Calc the frequent closed k-itemsets */
			calcClosedItemSets();
			itemSetsToGenerate = closedItemSets;
		} else if (method == Method.MAXIMAL) {
			/* Calc the frequent maximal k-itemsets */
			calcMaximalItemSets();
			itemSetsToGenerate = maximalItemSets;
		} else {
			itemSetsToGenerate = itemSets;
		}
		
		/* Display itemSets */
		displayItemSets(itemSetsToGenerate);
		
		/* Generate the rules */
		generateRules(itemSetsToGenerate);
		
		/* Display rules */
		displayRules();
		
	} /* End genAndDisplayRules */

	/* Calc closed itemsets */
	private void calcClosedItemSets() {
		for (Integer i : itemSets.keySet()) {
			if (itemSets.containsKey(i+1)) { // If the key exists
				List<ItemSet> itemSetsToPut = new ArrayList<ItemSet>();
				
				for (ItemSet is : itemSets.get(i)) { // For each i-itemset
					boolean isToPut = true;
					
					for (ItemSet itemset : itemSets.get(i+1)) { // Compare to i+1-itemset
						// Check if support is equal and items included
						if (is.isIncludedInto(itemset) && (is.getSupport()==itemset.getSupport())) {
							isToPut = false;
						}
					}
					
					if (isToPut) {
						itemSetsToPut.add(is);
					}
				} // end for each
				
				// Add the list if it isnt empty
				if (!itemSetsToPut.isEmpty()) {
					closedItemSets.put(i, itemSetsToPut);
				}
				
			} else { // Else we can add itemsets
				closedItemSets.put(i, itemSets.get(i));
			}
		}
	} /* End calcClosedItemSets */

	/* Calc maximal itemsets */
	private void calcMaximalItemSets() {
		for (Integer i : itemSets.keySet()) {
			if (itemSets.containsKey(i+1)) { // If the key exists
				List<ItemSet> itemSetsToPut = new ArrayList<ItemSet>();
				
				for (ItemSet is : itemSets.get(i)) { // For each i-itemset
					boolean isToPut = true;
					
					for (ItemSet itemset : itemSets.get(i+1)) { // Compare to i+1-itemset
						// Check if items included
						if (is.isIncludedInto(itemset)) {
							isToPut = false;
						}
					}
					
					if (isToPut) {
						itemSetsToPut.add(is);
					}
				} // end for each
				
				// Add the list if it isnt empty
				if (!itemSetsToPut.isEmpty()) {
					maximalItemSets.put(i, itemSetsToPut);
				}
				
			} else { // Else we can add itemsets
				maximalItemSets.put(i, itemSets.get(i));
			}
		}
	} /* End calcClosedItemSets */
	
	/* Display all rules */
	private void displayRules() {
		System.out.println(rules.size() + " rules found with a Min conf = " + minConf + " and min support = " + minSupport + "%");
		for (Rule r : rules)
			System.out.println(r);
	} /* End displayRules */

	/* Generate the rules */
	private void generateRules(Map<Integer, List<ItemSet>> itemSetsToGenRules) {
		rules = new ArrayList<Rule>();
		
		for (int i : itemSetsToGenRules.keySet()) {
			if (i > 1) { // 1-itemsets ignored
				for (ItemSet is : itemSetsToGenRules.get(i)){
					for (Item item : is.getItems()) {
						List<Item> hypothesis = new ArrayList<Item>(is.getItems());
						hypothesis.remove(item);

						// Calc the confidence
						double conf = calcConfidence(hypothesis, is.getSupport()); // confidence

						if (conf >= minConf)
							rules.add(new Rule(hypothesis, item, conf));
					}
				}
			}
		}
	} /* End generateRules */

	/* Calc the confidence of items */
	/* conf(A -> B) = Pr (AnB) / Pr (A) = Support(AnB) / Support (A) */
	private double calcConfidence(List<Item> a, int suppAB) {
		return  (double) suppAB / ((double) calcSupport(a)); 
		// Recalc the supp(a) : Maybe to optimize because the supp(a) exists with the existing itemset with item list a
	} /* End calcConfidence */

	/* Display all k-itemsets */
	private void displayItemSets(Map<Integer, List<ItemSet>> itemSetsToDisplay) {
		for (Integer i : itemSetsToDisplay.keySet()) {
			System.out.println(" Frequent "+i+"-itemsets : " + itemSetsToDisplay.get(i).size());

			int count = 1;
			for (ItemSet is : itemSetsToDisplay.get(i)){
				System.out.println("[" + (count++) + "]\t" + is);
			}
			System.out.println("--");
		}
	} /* End displayItemSets */

	/* initialize the frequent 1-itemsets */
	private void init1ItemSets() {
		// Create the first list
		ArrayList<ItemSet> itemSetsFound = new ArrayList<ItemSet>();

		for (Item i : Item.ITEMS.values())
			if((double)i.getSupp() >= minSupp) {
				ArrayList<Item> items = new ArrayList<Item>();
				items.add(i);
				itemSetsFound.add(new ItemSet(1, i.getSupp(), items));	
			}

		itemSets.put(1, itemSetsFound);

		/* DEBUG DISPLAY */
		//		System.out.println("size 1item: " + itemSetsFound.size());

	} /* End init1ItemSets */

	/* Calc the frequent k-itemsets */
	private void calcItemSets() {
		List<ItemSet> itemSetsFound;

		int k = 2;

		do {
			itemSetsFound = new ArrayList<ItemSet>();
			/* Calc the candidates to be frequent k itemset, with pruning */
			List<ItemSet> itemSetsCandidates = calcCandidates(k-1);

			/* DEBUG DISPLAY */
			//			System.out.println("k : " + k + " candidSize " + itemSetsCandidates.size());

			/* Calc the support for each candidate */
			for (ItemSet is : itemSetsCandidates) {
				int s = calcSupport(is);

				if(s >= minSupp) {
					is.setSupport(s);
					itemSetsFound.add(is);
				}
			}
			// End
			if (!itemSetsFound.isEmpty()) {
				itemSets.put(k, itemSetsFound);
			}

			/* DEBUG DISPLAY */
			//			System.out.println("k : " + k + " candidSize retenu " + itemSetsFound.size());

			k++;

		} while (!itemSetsFound.isEmpty());
	} /* End calcItemSets */

	/* Make the candidates itemsets */
	private List<ItemSet> calcCandidates(int kinf) {
		List<ItemSet> candidates = new ArrayList<ItemSet>();

		for (int i = 0 ; i < (itemSets.get(kinf).size() - 1); i++) {

			for (int j = (i + 1) ; j < itemSets.get(kinf).size() ; j++) {
				/* Add the itemset only if only one item of the 2 itemsets differs */
				List<Item> set = union(itemSets.get(kinf).get(i).getItems(), itemSets.get(kinf).get(j).getItems());

				if (set.size() == (kinf + 1)) {
					if (pruneAuthorized(set)) { // Check if pruning is ok
						ItemSet tmpIS = new ItemSet(kinf + 1);

						tmpIS.addItems(set);

						if (!candidates.contains(tmpIS))
							candidates.add(tmpIS);
					}
				}
			}
		}
		return candidates;
	} /* End calcCandidates */

	/* Make the union of two item list */
	private List<Item> union(List<Item> items, List<Item> items2) {
		List<Item> res = new ArrayList<Item>(items);

		res.removeAll(items2);
		res.addAll(items2);

		return res;
	} /* End union */

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
	} /* End pruneAuthorized */

	/* Calc the support of the given itemset */
	private Integer calcSupport(ItemSet is) {
		Integer result = 0;

		for (Transaction t : transactions) {
			if (t.getItems().containsAll(is.getItems())) {
				result++;
			}
		}
		return result;
	} /* End calcSupport */

	/* Calc the support of the given item list */
	private Integer calcSupport(List<Item> is) {
		Integer result = 0;

		for (Transaction t : transactions) {
			if (t.getItems().containsAll(is)) {
				result++;
			}
		}
		return result;
	} /* End calcSupport */

	/* Call the TransactionParser for each line */
	private void parseFile(String fileToRead) {
		TransactionParser tParser = null;
		BufferedReader bufferReader = null;
		Item.init();

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
			System.out.println("File " + fileToRead + " not found.");
			//e.printStackTrace();
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