package com.gpea;

public interface StaticConfig {

	String OUTPUT_FILE_NAME = "gplay_earnings_receipt_%s.pdf";

	String DISTRIBUTOR_GOOGLE_INC_INFO = 
			"Google Inc.\n"
			+ "1600 Amphitheatre Parkway\n"
			+ "Mountain View, CA\n"
			+ "USA\n"
			+ "VAT number: EU372000041";
	
	String DISTRIBUTOR_GOOGLE_COMMERCE_INFO = 
			"Google Commerce Ltd\n"
			+ "Gordon House, Barrow Street\n"
			+ "Dublin 4\n"
			+ "IRELAND\n"
			+ "VAT number: IE9825613N";
	
	String DISTRIBUTOR_GOOGLE_ASIA_PACIFIC_INFO = 
			"Google Asia Pacific Pte. Ltd\n"
			+ "8 Marina View\n"
			+ "Asia Square 1 #30-01\n"
			+ "SINGAPORE 018960\n"
			+ "GST/VAT number: 200817984R";
	
	String VAT_INFO_EUROPA =
			"The services provided in the EU are subject to the reverse charge mechanism"
			+ " and so VAT is to be accounted for the recipient Google, per Article 196"
			+ " of Council Directive 2006/112/EC.";
	
	String VAT_INFO_USA =
			"Merchants who have provided the foreigner status certificate (form W-8BEN) "
			+ "are exempt from the tax declaration obligation in the United States of America.";
	
}
