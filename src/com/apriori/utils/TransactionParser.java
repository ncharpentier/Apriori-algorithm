package com.apriori.utils;

import com.apriori.data.Item;
import com.apriori.data.Transaction;

public class TransactionParser {

	/* --- Attributes --- */
	private String[] tokens; // Refers to fields in the first line of the file
	/* --- /Attributes --- */

	/* --- Constructors --- */
	public TransactionParser(String[] tokens) {
		this.tokens = tokens;

		// Init the static map of the item class because it's a new file
		Item.init();
	}
	/* --- /Constructors --- */

	/* Parse a transaction string into a transaction object */
	public Transaction parseLine(String sTransaction, String splitter) 
			throws DataLengthNotConcordException {

		Transaction result = new Transaction();
		int counter = 0;
		
		/* DEBUG DISPLAY */
		// System.out.println(sTransaction);
		
		// Check the number of fields between the current transaction and tokens
		if (tokens.length != sTransaction.split(splitter).length)
			throw new DataLengthNotConcordException("Exception : the transaction " + sTransaction
					+ "\ndoesn't have the same number of fields ("
					+ sTransaction.split(splitter).length + "), compare to the tokens (" + tokens.length + ").");

		// Add each item to the transaction
		for (String s : sTransaction.split(splitter)) {
			if (!s.equals("0")) {
				result.addItem(Item.getItemByValue(tokens[counter], s));
			}
			
			counter++;
		}

		return result;
	} /* End parseLine */
}

/* Exception to determine if the current line got more tokens than the first line */
class DataLengthNotConcordException extends Exception {
	private static final long serialVersionUID = 1L;

	public DataLengthNotConcordException() {
		super();
	}

	public DataLengthNotConcordException(String s) {
		super(s);
	}
}
