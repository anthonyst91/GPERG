package com.gpea.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import com.gpea.model.Config;

public final class ConfigProvider {

	private static ConfigProvider _instance;
	
	private Config config;
	
	private ConfigProvider() {
		//singleton
	}
	
	public static synchronized ConfigProvider getInstance() {
		if (_instance == null) {
			_instance = new ConfigProvider();
		}
		return _instance;
	}
	
	public Config getConfig() throws IllegalStateException {
		if (config == null) {
			throw new IllegalStateException("config not loaded yet");
		}
		return config;
	}
	
	/**
	 * Load the config from the passed CSV file
	 * @param filePath The path to the CSV file for the config
	 * @return The loaded config
	 */
	public Config loadConfig(File file) {
		if (this.config != null) {
			return this.config;
		}
		
		config = new Config();
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			
			List<String> split;
			
		    String line = br.readLine();
		    line = br.readLine(); // ignore the first row, go to the second one

		    while (line != null) {
		    	split = CSVUtils.parseLine(line);
		    	config.setCompanyName(split.get(0));
		    	config.setStreet(split.get(1));
		    	config.setZipcode(split.get(2));
		    	config.setCity(split.get(3));
		    	config.setCountry(split.get(4));
		    	config.setVatNumber(split.get(5));
		    	config.setSalesPerdiod(split.get(6));
		    	config.setInvoiceNumber(split.get(7));
		    	config.setInvoiceDate(split.get(8));
		    	config.setPaymentDate(split.get(9));
		    	
		        line = br.readLine();
		    }
					
		} catch (Exception e) {
			System.err.print("loadConfig.exception while parsing the file: " + e.getMessage());

		} finally {
		    try {
				br.close();
			} catch (Exception e) {
				System.err.print("loadConfig.exception while closing br: " + e.getMessage());
			}
		}
		
		System.out.print("\n");
		System.out.print("Configuration: " + config.toString() + "\n");
		System.out.print("\n");
		
		return config;
	}

}
