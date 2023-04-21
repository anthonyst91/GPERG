package com.gpea.util;

import com.gpea.model.Config;
import com.gpea.model.VATStatus;

public final class AccountingUtils {

	private AccountingUtils() {
		//ignore
	}
	
	public static String toCompanyInfo(Config config) {
		return String.format("%s\n%s\n%s %s\n%s\nVAT number: %s",
				config.getCompanyName(), config.getStreet(), config.getZipcode(), 
				config.getCity(), config.getCountry(), config.getVatNumber());
	}
	
	public static String toAmountLine(float amount, String currency) {
		if (amount > 0) {
			return String.format("+ %.2f %s", amount, currency);
		} else if (amount < 0) {
			return String.format("- %.2f %s", -amount, currency);
		}
		return String.format("0 %s", currency);
	}
	
	public static String toVatStatusLine(VATStatus status) {
		switch (status) {
			case FULLY_PAID: return "self-liquidated";
			case BEHOLDEN: return "BEHOLDEN";
			case PARTIALLY_PAID: return "PARTIALLY PAID";
			case EXEMPTED: return "exempted";
			default: return "?";
		}
	}
	
}
