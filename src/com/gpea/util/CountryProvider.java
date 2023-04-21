package com.gpea.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.gpea.model.Country;
import com.gpea.model.USAState;

public final class CountryProvider {

	private static CountryProvider _instance;
	
	private List<Country> countries;
	private List<USAState> usaStates;
	
	private CountryProvider() {
		//singleton
	}
	
	public static synchronized CountryProvider getInstance() {
		if (_instance == null) {
			_instance = new CountryProvider();
		}
		return _instance;
	}
	
	/**
	 * Load the countries from the inner CSV file
	 * @return The list of countries with their ISO code
	 */
	public List<Country> loadCountries() {
		return loadCountries(false);
	}
	
	/**
	 * Load the countries from the inner CSV file
	 * @param logResult true to log the countries, false otherwise
	 * @return The list of countries with their ISO code
	 */
	public List<Country> loadCountries(boolean logResult) {
		if (this.countries != null) {
			return this.countries;
		}
		
		countries = new ArrayList<>();
		
		InputStream is = CountryProvider.class.getResourceAsStream("/countries.csv");
		if (is == null) {
			System.err.print("File 'countries.csv' not located\n\n");
			return countries;
		}
		
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = null;
		try {
			br = new BufferedReader(isr);
			
			Country country;
			List<String> split;
			
		    String line = br.readLine();
		    line = br.readLine(); // ignore the first row, go to the second one

		    while (line != null) {
		    	country = new Country();
		    	
		    	split = CSVUtils.parseLine(line);
		    	country.setName(split.get(0));
		    	country.setCode(split.get(1));
		    	country.setRegion(split.get(5));
		    	country.setSubRegion(split.get(6));
		    	country.setIntermediateRegion(split.get(7));
		    	country.setVatStatus(split.get(11));
		    	
		    	countries.add(country);
		    	
		        line = br.readLine();
		    }
					
		} catch (Exception e) {
			System.err.print("loadCountries.exception while parsing the file: " + e.getMessage());

		} finally {
		    try {
				br.close();
			} catch (Exception e) {
				System.err.print("loadCountries.exception while closing br: " + e.getMessage());
			}
		}
		
		if (logResult) {
			for (Country c: countries) {
				System.out.print(c.toString() + "\n");
			}
			
			System.out.print("\n");
			System.out.print("Amount of known countries: " + countries.size() + "\n");
			System.out.print("\n");
		}
		
		return countries;
	}
	
	/**
	 * Load the USA States from the inner CSV file
	 * @return The list of USA States with their ISO code
	 */
	public List<USAState> loadUsaStates() {
		return loadUsaStates(false);
	}
	
	/**
	 * Load the USA States from the inner CSV file
	 * @param logResult true to log the USA States, false otherwise
	 * @return The list of USA States with their ISO code
	 */
	public List<USAState> loadUsaStates(boolean logResult) {
		if (this.usaStates != null) {
			return this.usaStates;
		}
		
		usaStates = new ArrayList<>();
		
		InputStream is = CountryProvider.class.getResourceAsStream("/usa_states.csv");
		if (is == null) {
			System.err.print("File 'usa_states.csv' not located\n\n");
			return usaStates;
		}
		
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = null;
		try {
			br = new BufferedReader(isr);
			
			USAState state;
			List<String> split;
			
		    String line = br.readLine();
		    line = br.readLine(); // ignore the first row, go to the second one

		    while (line != null) {
		    	state = new USAState();
		    	
		    	split = CSVUtils.parseLine(line);
		    	state.setName(split.get(0));
		    	state.setCode(split.get(1));
		    	state.setVatStatus(split.get(3));
		    	
		    	usaStates.add(state);
		    	
		        line = br.readLine();
		    }
					
		} catch (Exception e) {
			System.err.print("loadUsaStates.exception while parsing the file: " + e.getMessage());

		} finally {
		    try {
				br.close();
			} catch (Exception e) {
				System.err.print("loadUsaStates.exception while closing br: " + e.getMessage());
			}
		}
		
		if (logResult) {
			for (USAState c: usaStates) {
				System.out.print(c.toString() + "\n");
			}
			
			System.out.print("\n");
			System.out.print("Amount of known USA States: " + usaStates.size() + "\n");
			System.out.print("\n");
		}
		
		return usaStates;
	}

	/**
	 * Get country object by the passed ISO code.
	 * @param isoCode The ISO code
	 * @return The country
	 */
	public Country getCountryByIsoCode(String isoCode) {
		if (countries == null) {
			loadCountries();
		}
		
		for (Country c: countries) {
			if (isoCode.equals(c.getCode())) {
				return c;
			}
		}
		
		return null;
	}
	
	/**
	 * Get USA State object by the passed ISO code.
	 * @param isoCode The ISO code
	 * @return The country
	 */
	public USAState getUSAStateByIsoCode(String isoCode) {
		if (usaStates == null) {
			loadUsaStates();
		}
		
		for (USAState s: usaStates) {
			if (isoCode.equals(s.getCode())) {
				return s;
			}
		}
		
		return null;
	}
	
}
