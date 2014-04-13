package com.apriori;

import java.io.File;

import com.apriori.utils.Miner;

public class Launcher {

	public static String FILE_TO_READ = "Resources/unstructured/transa.txt";
	public static double MIN_SUPPORT = 20;
	public static double MIN_CONF = 0.7;
	public static Method METHOD = Method.NORMAL;
	
	public static void main(String[] args) {
		
		if (args.length > 0) {
			for (int i = 0 ; i < args.length ; i++) {
				switch (args[i]) {
				case "-m":
					METHOD = Method.MAXIMAL;
					break;
				case "-c":
					METHOD = Method.CLOSED;
					break;
				case "-f":
					try {
						FILE_TO_READ = args[++i];
						if (!new File(FILE_TO_READ).isFile()) {
							throw new Exception(FILE_TO_READ);
						}
					} catch (Exception e) {
						System.out.println("File " + e.getMessage() + " not found.");
						return;
					}
					break;
				case "-s":
					try {
						MIN_SUPPORT = Double.parseDouble(args[++i]);
					} catch (Exception e) {
						System.out.println("Invalid support.");
						return;
					}
					break;
				case "-co":
					try {
						MIN_CONF = Double.parseDouble(args[++i]);
					} catch (Exception e) {
						System.out.println("Invalid confidence.");
						return;
					}
					break;
				default :
					System.out.println("Arguments : " + args[i] + " unknow.");
					displaySynthax();
					return;
				}
			}
		} else {
			System.out.println("Default use (supp : 20%, confidence : 0.7, frequent itemset method)");
			displaySynthax();
		}
		
		Miner miner = new Miner(MIN_SUPPORT,MIN_CONF);
		
		miner.processFile(FILE_TO_READ);
		miner.genAndDisplayRules(METHOD);
	}
	
	public static void displaySynthax() {
		System.out.println("Synthax : \n\tcommand -[c|m] -f <file> -s <support> -co <confidence>");
		System.out.println("-c : Closed ItemSet Method\n"
				+ "-m : Maximal ItemSet Method\n"
				+ "-f <file> : specify the file wanted\n"
				+ "-s <support> : specify the support wanted (double value between 0 and 100)\n"
				+ "-co <confidence> : specify the confidence wanted (double value between 0 and 1)\n");
	}
	
	public enum Method {
		  NORMAL,
		  CLOSED,
		  MAXIMAL;
	}
}
