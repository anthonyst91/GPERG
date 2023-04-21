package com.gpea.processor;

import java.util.List;
import java.util.Map;

import com.gpea.model.Country;
import com.gpea.model.Transaction;
import com.gpea.model.TransactionsDetails;
import com.gpea.model.USAState;
import com.gpea.model.VATStatus;
import com.gpea.model.Transaction.Type;
import com.gpea.util.TransactionUtils;

public class ReportLogger {
	
	public void logChargesByCountry(List<Transaction> transactions) {
		int chargeCount = 0;
		int chargeRefundCount = 0;
		for (Transaction transaction : transactions) {
			System.out.print(transaction.toString() + "\n");
			if (transaction.getType() == Type.CHARGE) chargeCount++;
			if (transaction.getType() == Type.CHARGE_REFUND) chargeRefundCount++;
		}

		System.out.print("\n");
		System.out.print("Amount of charges (with no refund): " + (chargeCount - chargeRefundCount) + "\n");
		System.out.print("Amount of charges refunded: " + chargeRefundCount + "\n");
		System.out.print("\n");
	}
	
	public void logReportByCountry(Map<Country, List<Transaction>> transactions) {
		System.out.print("///////////// REPORT BY COUNTRY /////////////\n\n");
		System.out.print("Amount of countries: " + transactions.size() + "\n\n");
		
		StringBuilder sb = new StringBuilder();
		float amount = 0f;
		float totalAmount = 0f;
		TransactionsDetails details;
		String merchantCurrency = "";
		
		for (Map.Entry<Country, List<Transaction>> entry : transactions.entrySet()) {
			sb.setLength(0);
			amount = 0f;
			
			for (Transaction t: entry.getValue()) {
				amount += t.getMerchantAmount();
				merchantCurrency = t.getMerchantCurrency();
			}
			totalAmount += amount;
			
			details = TransactionUtils.getDetails(entry.getValue());
			
			sb.append(String.format("%s [%s] : ", entry.getKey().getName(), entry.getKey().getCode()));
			sb.append(details.toString());
			if (entry.getKey().getVatStatus() == VATStatus.PARTIALLY_PAID) sb.append(" (VAT partially paid)");
			if (entry.getKey().getVatStatus() == VATStatus.BEHOLDEN) sb.append(" (VAT beholden)");
			sb.append("\n");
			
			System.out.print(sb.toString());
		}
		
		System.out.print("\n");
		System.out.print(String.format("Total amount: %.2f %s\n", totalAmount, merchantCurrency));
		System.out.print("\n");
	}
	
	public void logReportByRegion(Map<Country.Region, List<Transaction>> transactions) {
		System.out.print("///////////// REPORT BY REGION /////////////\n\n");
		System.out.print("Amount of regions: " + transactions.size() + "\n\n");
		
		StringBuilder sb = new StringBuilder();
		float amount = 0f;
		float totalAmount = 0f;
		TransactionsDetails details;
		String merchantCurrency = "";
		
		for (Map.Entry<Country.Region, List<Transaction>> entry : transactions.entrySet()) {
			sb.setLength(0);
			amount = 0f;
			
			for (Transaction t: entry.getValue()) {
				amount += t.getMerchantAmount();
				merchantCurrency = t.getMerchantCurrency();
			}
			totalAmount += amount;
			
			details = TransactionUtils.getDetails(entry.getValue());
			
			sb.append(entry.getKey().name()).append(" : ");
			sb.append(details.toString());
			sb.append("\n");
			
			System.out.print(sb.toString());
		}
		
		System.out.print("\n");
		System.out.print(String.format("Total amount: %.2f %s", totalAmount, merchantCurrency));
		System.out.print("\n");
	}
	
	public void logReportByRegionAndCountry(Map<Country.Region, Map<Country, List<Transaction>>> transactions) {
		System.out.print("///////////// REPORT BY REGION AND COUNTRY /////////////\n\n");
		System.out.print("Amount of regions: " + transactions.size() + "\n\n");
		
		StringBuilder sb = new StringBuilder();
		TransactionsDetails details;
		float amount = 0f;
		float regionAmount = 0f;
		float worldAmount = 0f;
		String merchantCurrency = "";
		
		// Browse through regions
		for (Map.Entry<Country.Region, Map<Country, List<Transaction>>> regionEntry : transactions.entrySet()) {
			System.out.print(String.format("////// Region %s (%d countries) //////\n\n", regionEntry.getKey(), regionEntry.getValue().size()));

			regionAmount = 0f;
			
			// Browser through countries of the region
			for (Map.Entry<Country, List<Transaction>> countryEntry : regionEntry.getValue().entrySet()) {
				sb.setLength(0);
				amount = 0f;
				
				for (Transaction t: countryEntry.getValue()) {
					amount += t.getMerchantAmount();
					merchantCurrency = t.getMerchantCurrency();
				}
				regionAmount += amount;
				worldAmount += amount;
				
				details = TransactionUtils.getDetails(countryEntry.getValue());
				
				sb.append(String.format("- %s [%s] : ", countryEntry.getKey().getName(), countryEntry.getKey().getCode()));
				sb.append(details.toString());
				if (countryEntry.getKey().getVatStatus() == VATStatus.PARTIALLY_PAID) sb.append(" (VAT partially paid)");
				if (countryEntry.getKey().getVatStatus() == VATStatus.BEHOLDEN) sb.append(" (VAT beholden)");
				sb.append("\n");
				
				System.out.print(sb.toString());
			}
			
			System.out.print("\n");
			System.out.print(String.format("Total region amount: %.2f %s\n", regionAmount, merchantCurrency));
			System.out.print("\n");
		}
		
		System.out.print("\n");
		System.out.print(String.format("Total world amount: %.2f %s\n", worldAmount, merchantCurrency));
		System.out.print("\n");
	}
	
	public void logReportByDistributorAndCountry(Map<Country.Distributor, Map<Country, List<Transaction>>> transactions) {
		System.out.print("///////////// REPORT BY DISTRIBUTOR AND COUNTRY /////////////\n\n");
		System.out.print("Amount of distributors: " + transactions.size() + "\n\n");
		
		StringBuilder sb = new StringBuilder();
		TransactionsDetails details;
		float amount = 0f;
		float regionAmount = 0f;
		float worldAmount = 0f;
		String merchantCurrency = "";
		
		// Browse through regions
		for (Map.Entry<Country.Distributor, Map<Country, List<Transaction>>> regionEntry : transactions.entrySet()) {
			System.out.print(String.format("////// Distributor %s (%d countries) //////\n\n", regionEntry.getKey(), regionEntry.getValue().size()));

			regionAmount = 0f;
			
			// Browser through countries of the region
			for (Map.Entry<Country, List<Transaction>> countryEntry : regionEntry.getValue().entrySet()) {
				sb.setLength(0);
				amount = 0f;
				
				for (Transaction t: countryEntry.getValue()) {
					amount += t.getMerchantAmount();
					merchantCurrency = t.getMerchantCurrency();
				}
				regionAmount += amount;
				worldAmount += amount;
				
				details = TransactionUtils.getDetails(countryEntry.getValue());
				
				sb.append(String.format("- %s [%s] : ", countryEntry.getKey().getName(), countryEntry.getKey().getCode()));
				sb.append(details.toString());
				if (countryEntry.getKey().getVatStatus() == VATStatus.PARTIALLY_PAID) sb.append(" (VAT partially paid)");
				if (countryEntry.getKey().getVatStatus() == VATStatus.BEHOLDEN) sb.append(" (VAT beholden)");
				sb.append("\n");
				
				System.out.print(sb.toString());
			}
			
			System.out.print("\n");
			System.out.print(String.format("Total region amount: %.2f %s\n", regionAmount, merchantCurrency));
			System.out.print("\n");
		}
		
		System.out.print("\n");
		System.out.print(String.format("Total world amount: %.2f %s\n", worldAmount, merchantCurrency));
		System.out.print("\n");
	}
	
	public void logReportByUSAState(Map<USAState, List<Transaction>> transactions) {
		System.out.print("///////////// REPORT BY USA STATE /////////////\n\n");
		System.out.print("Amount of states: " + transactions.size() + "\n\n");
		
		StringBuilder sb = new StringBuilder();
		float amount = 0f;
		float totalAmount = 0f;
		TransactionsDetails details;
		String merchantCurrency = "";
		
		for (Map.Entry<USAState, List<Transaction>> entry : transactions.entrySet()) {
			sb.setLength(0);
			amount = 0f;
			
			for (Transaction t: entry.getValue()) {
				amount += t.getMerchantAmount();
				merchantCurrency = t.getMerchantCurrency();
			}
			totalAmount += amount;
			
			details = TransactionUtils.getDetails(entry.getValue());
			
			sb.append(String.format("- %s [%s] : ", entry.getKey().getName(), entry.getKey().getCode()));
			sb.append(details.toString());
			if (entry.getKey().getVatStatus() == VATStatus.BEHOLDEN) sb.append(" (VAT beholden)");
			sb.append("\n");
			
			System.out.print(sb.toString());
		}
		
		System.out.print("\n");
		System.out.print(String.format("Total amount for USA: %.2f %s\n", totalAmount, merchantCurrency));
		System.out.print("\n");
	}

}
