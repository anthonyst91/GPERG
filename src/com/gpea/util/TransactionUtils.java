package com.gpea.util;

import java.util.List;
import java.util.Map;

import com.gpea.model.Country;
import com.gpea.model.Transaction;
import com.gpea.model.TransactionsDetails;

public final class TransactionUtils {
	
	private TransactionUtils() {
		//ignore
	}
	
	/**
	 * Compute all the details relative to the passed list of transactions (using the merchant amounts)
	 * @param transactions The transactions for the computations
	 * @param currency The currency
	 * @return The details for the passed transactions
	 */
	public static TransactionsDetails getDetails(List<Transaction> transactions) {		
		String currency = "";
		
		int chargeCpt = 0, chargeRefundCpt = 0;
		float chargeAmount = 0, chargeRefundAmount = 0;
		
		int feesCpt = 0, feeRefundsCpt = 0;
		float feesAmount = 0, feesRefundAmount = 0;
		
		int taxesCpt = 0, taxRefundsCpt = 0;
		float taxesAmount = 0, taxRefundsAmount = 0;
		
		for (Transaction t: transactions) {
			currency = t.getMerchantCurrency();
			
			switch (t.getType()) {
				case CHARGE:
					chargeCpt++;
					chargeAmount += t.getMerchantAmount(); 
					break;
					
				case CHARGE_REFUND:
					chargeRefundCpt++;
					chargeRefundAmount += t.getMerchantAmount(); 
					break;
					
				case FEE:
					feesCpt++;
					feesAmount += t.getMerchantAmount(); 
					break;
					
				case FEE_REFUND:
					feeRefundsCpt++;
					feesRefundAmount += t.getMerchantAmount(); 
					break;
					
				case TAX:
					taxesCpt++;
					taxesAmount += t.getMerchantAmount(); 
					break;
					
				case TAX_REFUND:
					taxRefundsCpt++;
					taxRefundsAmount += t.getMerchantAmount(); 
					break;
					
				default: 
					continue;
			}
		}
		
		TransactionsDetails details = new TransactionsDetails(currency);
		
		details.setCharges(chargeCpt);
		details.setChargeRefunds(chargeRefundCpt);
		details.setAmountCharges(chargeAmount);
		details.setAmountChargeRefunds(chargeRefundAmount);
		
		details.setGoogleFees(feesCpt);
		details.setGoogleFeeRedunds(feeRefundsCpt);
		details.setAmountGoogleFees(feesAmount);
		details.setAmountGoogleFeeRefunds(feesRefundAmount);
		
		details.setTaxes(taxesCpt);
		details.setTaxRefunds(taxRefundsCpt);
		details.setAmountTaxes(taxesAmount);
		details.setAmountTaxRefunds(taxRefundsAmount);
		
		return details;
	}
	
	/**
	 * Compute all the details relative to the passed list of transactions per country (using the merchant amounts)
	 * @param transactions The transactions per country for the computations
	 * @param currency The currency
	 * @return The details for the passed transactions
	 */
	public static TransactionsDetails getDetails(Map<Country, List<Transaction>> transactions) {
		String currency = "";
		
		int chargeCpt = 0, chargeRefundCpt = 0;
		float chargeAmount = 0, chargeRefundAmount = 0;
		
		int feesCpt = 0, feeRefundsCpt = 0;
		float feesAmount = 0, feesRefundAmount = 0;
		
		int taxesCpt = 0, taxRefundsCpt = 0;
		float taxesAmount = 0, taxRefundsAmount = 0;

		for (Map.Entry<Country, List<Transaction>> entry: transactions.entrySet()) {
			for (Transaction t: entry.getValue()) {
				currency = t.getMerchantCurrency();
				
				switch (t.getType()) {
					case CHARGE:
						chargeCpt++;
						chargeAmount += t.getMerchantAmount(); 
						break;
						
					case CHARGE_REFUND:
						chargeRefundCpt++;
						chargeRefundAmount += t.getMerchantAmount(); 
						break;
						
					case FEE:
						feesCpt++;
						feesAmount += t.getMerchantAmount(); 
						break;
						
					case FEE_REFUND:
						feeRefundsCpt++;
						feesRefundAmount += t.getMerchantAmount(); 
						break;
						
					case TAX:
						taxesCpt++;
						taxesAmount += t.getMerchantAmount(); 
						break;
						
					case TAX_REFUND:
						taxRefundsCpt++;
						taxRefundsAmount += t.getMerchantAmount(); 
						break;
						
					default: 
						continue;
				}
			}
		}
		
		TransactionsDetails details = new TransactionsDetails(currency);
		
		details.setCharges(chargeCpt);
		details.setChargeRefunds(chargeRefundCpt);
		details.setAmountCharges(chargeAmount);
		details.setAmountChargeRefunds(chargeRefundAmount);
		
		details.setGoogleFees(feesCpt);
		details.setGoogleFeeRedunds(feeRefundsCpt);
		details.setAmountGoogleFees(feesAmount);
		details.setAmountGoogleFeeRefunds(feesRefundAmount);
		
		details.setTaxes(taxesCpt);
		details.setTaxRefunds(taxRefundsCpt);
		details.setAmountTaxes(taxesAmount);
		details.setAmountTaxRefunds(taxRefundsAmount);
		
		return details;
	}

}
