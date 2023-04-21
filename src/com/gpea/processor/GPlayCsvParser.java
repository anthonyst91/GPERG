package com.gpea.processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.gpea.model.Transaction;
import com.gpea.util.CSVUtils;

public class GPlayCsvParser {
	
	private ReportLogger logger;
	
	public GPlayCsvParser() {
		logger = new ReportLogger();
	}
	
	/**
	 * Parse the passed CSV file.
	 * @param file The file to be parsed
	 * @param logResult true to log the result, false otherwise
	 * @return The list of transactions
	 */
	public List<Transaction> parse(File file, boolean logResult) {
		ArrayList<Transaction> transactions = new ArrayList<>();
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));

			Transaction transaction;
			List<String> split;
			
		    String line = br.readLine();
		    line = br.readLine(); // ignore the first row, go to the second one

		    while (line != null) {
		    	transaction = new Transaction();
		    	
		    	split = CSVUtils.parseLine(line);
		    	transaction.setId(split.get(0));
		    	transaction.setDate(split.get(1));
		    	transaction.setTime(split.get(2));
		    	transaction.setTaxType(split.get(3));
		    	transaction.setType(split.get(4));
		    	transaction.setRefundType(split.get(5));
		    	transaction.setProductTitle(split.get(6));
		    	transaction.setProductId(split.get(7));
		    	transaction.setProductType(split.get(8));
		    	transaction.setSkuId(split.get(9));
		    	transaction.setBuyerHardware(split.get(10));
		    	transaction.setBuyerCountry(split.get(11));
		    	transaction.setBuyerState(split.get(12));
		    	transaction.setBuyerPostalCode(split.get(13));
		    	transaction.setBuyerCurrency(split.get(14));
		    	transaction.setBuyerAmount(split.get(15));
		    	transaction.setConversionRate(split.get(16));
		    	transaction.setMerchantCurrency(split.get(17));
		    	transaction.setMerchantAmount(split.get(18));
		    	
		    	transactions.add(transaction);
		    	
		        line = br.readLine();
		    }
		    
		} catch (IOException e) {
			System.err.print("parse.exception while parsing the file: " + e.getMessage());

		} finally {
		    try {
				br.close();
			} catch (Exception e) {
				System.err.print("parse.exception while closing br: " + e.getMessage());
			}
		}
		
		if (logResult) {
			logger.logChargesByCountry(transactions);
		}
		
		return transactions;
	}

}
