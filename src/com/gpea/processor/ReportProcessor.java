package com.gpea.processor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.gpea.model.Country;
import com.gpea.model.Transaction;
import com.gpea.model.USAState;
import com.gpea.util.CountryProvider;

public class ReportProcessor {
	
	private ReportLogger logger;
	
	public ReportProcessor() {
		logger = new ReportLogger();
	}

	/**
	 * Sort transactions by country in a map
	 * @param transactions The input
	 * @param true to log the report, false otherwise
	 * @return the transactions sorted by country in a map
	 */
	public Map<Country, List<Transaction>> reportByCountry(List<Transaction> transactions, boolean logResult) {
		Map<Country, List<Transaction>> result = new HashMap<>();
		
		List<Transaction> newList;
		
		for (Transaction t: transactions) {
			if (result.containsKey(t.getBuyerCountry())) {
				result.get(t.getBuyerCountry()).add(t);
			
			} else {
				newList = new ArrayList<>();
				newList.add(t);
				result.put(t.getBuyerCountry(), newList);
			}
		}
		
		// Sort the map keys alphabetically
        Map<Country, List<Transaction>> treeMap = new TreeMap<Country, List<Transaction>>(
                new Comparator<Country>() {
                    @Override
                    public int compare(Country c1, Country c2) {
                        return c1.getName().compareTo(c2.getName());
                    }
                });
        treeMap.putAll(result);
		
		if (logResult) {
			logger.logReportByCountry(treeMap);
		}
		
		return treeMap;
	}
	
	/**
	 * Sort transactions by region in a map
	 * @param transactions The input
	 * @param true to log the report, false otherwise
	 * @return the transactions sorted by region in a map
	 */
	public Map<Country.Region, List<Transaction>> reportByRegion(List<Transaction> transactions, boolean logResult) {
		Map<Country.Region, List<Transaction>> result = new HashMap<>();
		
		List<Transaction> newList;
		
		for (Transaction t: transactions) {
			if (result.containsKey(t.getBuyerCountry().getRegion())) {
				result.get(t.getBuyerCountry().getRegion()).add(t);
			
			} else {
				newList = new ArrayList<>();
				newList.add(t);
				result.put(t.getBuyerCountry().getRegion(), newList);
			}
		}
		
		// Sort the map keys alphabetically
        Map<Country.Region, List<Transaction>> treeMap = new TreeMap<Country.Region, List<Transaction>>(
                new Comparator<Country.Region>() {
                    @Override
                    public int compare(Country.Region cr1, Country.Region cr2) {
                        return cr1.name().compareTo(cr2.name());
                    }
                });
        treeMap.putAll(result);
		
		if (logResult) {
			logger.logReportByRegion(result);
		}
		
		return result;
	}
	
	/**
	 * Sort transactions by region and country in a map
	 * @param transactions The input
	 * @param true to log the report, false otherwise
	 * @return the transactions sorted by region and country in a map
	 */
	public Map<Country.Region, Map<Country, List<Transaction>>> reportByRegionAndCountry(List<Transaction> transactions, boolean logResult) {
		Map<Country.Region, Map<Country, List<Transaction>>> result = new HashMap<>();
		
		Map<Country, List<Transaction>> countries;
		List<Transaction> newList;
		
		for (Transaction t: transactions) {
			// Find if the region exists
			if (result.containsKey(t.getBuyerCountry().getRegion())) {
				countries = result.get(t.getBuyerCountry().getRegion());
				
				// Find if the country exists
				if (countries.containsKey(t.getBuyerCountry())) {
					countries.get(t.getBuyerCountry()).add(t);
				
				// If the country does not exist yet
				} else {
					newList = new ArrayList<>();
					newList.add(t);
					countries.put(t.getBuyerCountry(), newList);
				}
				
			// If the region does not exist yet
			} else {
				countries = new HashMap<>();
				newList = new ArrayList<>();
				newList.add(t);
				countries.put(t.getBuyerCountry(), newList);
				result.put(t.getBuyerCountry().getRegion(), countries);
			}
		}
		
		// Sort the map keys alphabetically
        Map<Country.Region, Map<Country, List<Transaction>>> treeMap = new TreeMap<>(
                new Comparator<Country.Region>() {
                    @Override
                    public int compare(Country.Region cr1, Country.Region cr2) {
                        return cr1.name().compareTo(cr2.name());
                    }
                });
        treeMap.putAll(result);
		
		if (logResult) {
			logger.logReportByRegionAndCountry(treeMap);
		}
		
		return treeMap;
	}
	
	/**
	 * Sort transactions by distributor and country in a map
	 * @param transactions The input
	 * @param true to log the report, false otherwise
	 * @return the transactions sorted by distributor and country in a map
	 */
	public Map<Country.Distributor, Map<Country, List<Transaction>>> reportByDistributorAndCountry(List<Transaction> transactions, boolean logResult) {
		Map<Country.Distributor, Map<Country, List<Transaction>>> result = new HashMap<>();
		
		Map<Country, List<Transaction>> countries;
		List<Transaction> newList;
		
		for (Transaction t: transactions) {
			// Find if the region exists
			if (result.containsKey(t.getBuyerCountry().getDistributor())) {
				countries = result.get(t.getBuyerCountry().getDistributor());
				
				// Find if the country exists
				if (countries.containsKey(t.getBuyerCountry())) {
					countries.get(t.getBuyerCountry()).add(t);
				
				// If the country does not exist yet
				} else {
					newList = new ArrayList<>();
					newList.add(t);
					countries.put(t.getBuyerCountry(), newList);
				}
				
			// If the region does not exist yet
			} else {
				countries = new HashMap<>();
				newList = new ArrayList<>();
				newList.add(t);
				countries.put(t.getBuyerCountry(), newList);
				result.put(t.getBuyerCountry().getDistributor(), countries);
			}
		}
		
		// Sort the map keys of transactions per country alphabetically
		Map<Country.Distributor, Map<Country, List<Transaction>>> sortedMap = new HashMap<>();
		Map<Country, List<Transaction>> treeMap;
		for (Map.Entry<Country.Distributor, Map<Country, List<Transaction>>> entry: result.entrySet()) {
	        treeMap = new TreeMap<Country, List<Transaction>>(
	                new Comparator<Country>() {
	                    @Override
	                    public int compare(Country c1, Country c2) {
	                        return c1.getName().compareTo(c2.getName());
	                    }
	                });
	        treeMap.putAll(entry.getValue());
	        sortedMap.put(entry.getKey(), treeMap);
		}
		
		// Sort the map key of distributors by ordinal
        Map<Country.Distributor, Map<Country, List<Transaction>>> fullySortedMap = new TreeMap<>(
                new Comparator<Country.Distributor>() {
                    @Override
                    public int compare(Country.Distributor d1, Country.Distributor d2) {
                        return d1.ordinal() - d2.ordinal();
                    }
                });
        fullySortedMap.putAll(result);
		
		if (logResult) {
			logger.logReportByDistributorAndCountry(fullySortedMap);
		}
		
		return fullySortedMap;
	}
	
	/**
	 * Sort transactions by USA State in a map
	 * @param transactions The input
	 * @param true to log the report, false otherwise
	 * @return the transactions sorted by USA State in a map
	 */
	public Map<USAState, List<Transaction>> reportByUSAState(List<Transaction> transactions, boolean logResult) {
		Map<USAState, List<Transaction>> result = new HashMap<>();
		
		List<Transaction> newList;
		USAState state;
		
		for (Transaction t: transactions) {
			// Consider only transactions from USA
			if (Country.USA_CODE.equals(t.getBuyerCountry().getCode())) {
				state = CountryProvider.getInstance().getUSAStateByIsoCode(t.getBuyerState());
				
				if (state == null) {
					System.err.print("\n!!! reportByUSAState.unknown state code: " + t.getBuyerState() + "\n");
				}

				if (result.containsKey(state)) {
					result.get(state).add(t);
				
				} else {
					newList = new ArrayList<>();
					newList.add(t);
					result.put(state, newList);
				}
			}
		}
		
		// Sort the map keys alphabetically
        Map<USAState, List<Transaction>> treeMap = new TreeMap<USAState, List<Transaction>>(
                new Comparator<USAState>() {
                    @Override
                    public int compare(USAState s1, USAState s2) {
                        return s1.getName().compareTo(s2.getName());
                    }
                });
        treeMap.putAll(result);
		
		if (logResult) {
			logger.logReportByUSAState(treeMap);	
		}
		
		return treeMap;
	}
	
}
