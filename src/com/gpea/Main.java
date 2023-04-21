package com.gpea;

import java.io.File;
import java.util.List;

import com.gpea.generator.PdfReportGenerator;
import com.gpea.model.Transaction;
import com.gpea.processor.GPlayCsvParser;
import com.gpea.processor.ReportProcessor;
import com.gpea.util.ConfigProvider;
import com.gpea.util.CountryProvider;

public class Main {
	
	private static final boolean DEBUG_COUNTRIES = false;
	private static final boolean DEBUG_TRANSACTIONS = false;
	private static final boolean DEBUG_BY_COUNTRY = false;
	private static final boolean DEBUG_BY_REGION = false;
	private static final boolean DEBUG_BY_REGION_AND_COUNTRY = true;
	private static final boolean DEBUG_BY_DISTRIBUTOR_AND_COUNTRY = true;
	private static final boolean DEBUG_BY_USA_STATE = true;
	
	private static GPlayCsvParser parser = new GPlayCsvParser();
	private static ReportProcessor reporter = new ReportProcessor();
	
	/**
	 * Main function, it has to collect the file to be parsed
	 * @param args
	 */
	public static void main(String[] args) {
		// Get the first argument (has to be the report file to be parsed)
		String reportFilePath = getReportFileFromArgument(args);
		if (reportFilePath == null) {
			System.exit(0);
			return;
		}
		
		// Get the first argument (has to be the configuration file to be parsed)
		String configFilePath = getConfigFileFromArgument(args);
		if (configFilePath == null) {
			System.exit(0);
			return;
		}
		
		// Load the countries, states and configuration
		CountryProvider.getInstance().loadCountries(DEBUG_COUNTRIES);
		CountryProvider.getInstance().loadUsaStates(DEBUG_COUNTRIES);
		ConfigProvider.getInstance().loadConfig(new File(configFilePath));
		
		// Now parse the file
		List<Transaction> transactions = parser.parse(new File(reportFilePath), DEBUG_TRANSACTIONS);
		debugTransactionReports(transactions);
		
		// Generate report in a PDF file
		PdfReportGenerator generator = new PdfReportGenerator(transactions);
		generator.generate(true);
		generator.generate(false);
	}
	
	private static String getReportFileFromArgument(String[] args) {
		try {
			String filePath = args[0];
			System.out.print("Report file: " + filePath + "\n\n");
			return filePath;
			
		} catch (IndexOutOfBoundsException e) {
			System.err.print("The report file (CSV) is missing\n\n");
			return null;
		}
	}
	
	private static String getConfigFileFromArgument(String[] args) {
		try {
			String filePath = args[1];
			System.out.print("Configuration file: " + filePath + "\n\n");
			return filePath;
			
		} catch (IndexOutOfBoundsException e) {
			System.err.print("The config file (CSV) is missing\n\n");
			return null;
		}
	}

	private static void debugTransactionReports(List<Transaction> transactions) {
		// Show report by region
		if (DEBUG_BY_REGION) {
			reporter.reportByRegion(transactions, true);
		}

		// Show report by country
		if (DEBUG_BY_COUNTRY) {
			reporter.reportByCountry(transactions, true);
		}

		// Show report by region and country
		if (DEBUG_BY_REGION_AND_COUNTRY) {
			reporter.reportByRegionAndCountry(transactions, true);
		}
		
		// Show report by distributor and country
		if (DEBUG_BY_DISTRIBUTOR_AND_COUNTRY) {
			reporter.reportByDistributorAndCountry(transactions, true);
		}
		
		// Show report by USA state
		if (DEBUG_BY_USA_STATE) {
			reporter.reportByUSAState(transactions, true);
		}
	}
	
}
