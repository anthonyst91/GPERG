package com.gpea.generator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

import com.gpea.StaticConfig;
import com.gpea.model.Config;
import com.gpea.model.Country;
import com.gpea.model.Transaction;
import com.gpea.model.TransactionsDetails;
import com.gpea.model.USAState;
import com.gpea.model.VATStatus;
import com.gpea.processor.ReportProcessor;
import com.gpea.util.AccountingUtils;
import com.gpea.util.ConfigProvider;
import com.gpea.util.TransactionUtils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfReportGenerator {
	
	/////////////////////
	// CONSTANT VALUES //
	/////////////////////
	
	private static final int TITLE_SIZE = 22;
	private static final int SUBTITLE_SIZE = 18;
	private static final int BODY_SIZE = 10;
	
	private static final int CELL_PADDING = 4;
	private static final int DEFAULT_SPACING_AFTER = 17;
	
	////////////////
	// ATTRIBUTES //
	////////////////
	
	private ReportProcessor reporter = new ReportProcessor();

	private List<Transaction> transactions;
	
	private Font fontTitle = FontFactory.getFont(FontFactory.COURIER_BOLD, TITLE_SIZE, BaseColor.BLACK);
	private Font fontSubTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, SUBTITLE_SIZE, BaseColor.BLACK);
	private Font fontBody1 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, BODY_SIZE, BaseColor.BLACK);
	private Font fontBody2 = FontFactory.getFont(FontFactory.HELVETICA, BODY_SIZE, BaseColor.BLACK);
	private Font fontCaption = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, BODY_SIZE, BaseColor.DARK_GRAY);
	
	//////////////////
	// CONSTRUCTORS //
	//////////////////
	
	public PdfReportGenerator(List<Transaction> transactions) {
		this.reporter = new ReportProcessor();
		this.transactions = transactions;
	}
	
	//////////////////////
	// BUSINESS METHODS //
	//////////////////////
	
	public void generate(boolean fullReport) {
		System.out.print("///////////// GENERATE PDF REPORT (full: " + fullReport + ") /////////////\n\n");
		
		String filename = String.format(Locale.getDefault(), StaticConfig.OUTPUT_FILE_NAME, fullReport ? "full" : "basic");
		Document document = createDocument(filename);
		if (document == null) return;
		
		try {
			// Generate main report
			Map<Country.Distributor, Map<Country, List<Transaction>>> distributors = 
					reporter.reportByDistributorAndCountry(transactions, false);
			buildMainReport(document, distributors);
			
			if (fullReport) {
				// Generate report for distributors
				document.newPage();
				buildDistributorsReport(document, distributors);
				
				// Generate report for countries
				document.newPage();
				buildCountriesReport(document, distributors);

				// Generate report for USA States (if needed)
				Map<USAState, List<Transaction>> usa = 
						reporter.reportByUSAState(transactions, false);
				if (!usa.isEmpty()) {
					document.newPage();
					buildUSAReport(document, usa);
				}
			}
			
		} catch (DocumentException e) {
			System.err.print("buildMainReport.document error: " + e.getMessage() + "\n");
		
		} finally {
			document.close();
		}
		
	}
	
	/* MAIN REPORT */
	
	private void buildMainReport(Document document,
			Map<Country.Distributor, Map<Country, List<Transaction>>> transactions) throws DocumentException {
		
		// Invoice header
		document.add(generatePageTitle("GOOGLE PLAY EARNINGS RECEIPT"));
		document.add(generateInvoiceInformation());
		document.add(generatePartnersInformation(StaticConfig.DISTRIBUTOR_GOOGLE_COMMERCE_INFO));
		
		// Total report
		document.add(generateTotalSubtitle());
		document.add(generateTotalReport());

		// Mentions regarding VAT
		document.add(Chunk.NEWLINE);
		document.add(generateCaption(StaticConfig.VAT_INFO_EUROPA));
		document.add(generateCaption(StaticConfig.VAT_INFO_USA));
	}
	
	private PdfPTable generateInvoiceInformation() throws DocumentException {
		PdfPTable table = new PdfPTable(2);
		
		Config config = ConfigProvider.getInstance().getConfig();
		
		table.addCell(newBodyCell("Sales period:", true, false, false));
		table.addCell(newBodyCell(config.getSalesPerdiod(), false, false, false));
		table.addCell(newBodyCell("Invoice Number:", true, false, false));
		table.addCell(newBodyCell(config.getInvoiceNumber(), false, false, false));
		table.addCell(newBodyCell("Invoice Date:", true, false, false));
		table.addCell(newBodyCell(config.getInvoiceDate(), false, false, false));
		table.addCell(newBodyCell("Payment Date:", true, false, false));
		table.addCell(newBodyCell(config.getPaymentDate(), false, false, false));
		
		table.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.setWidths(new float[] { 2, 5 });
		table.setWidthPercentage(100f);
		table.setSpacingAfter(DEFAULT_SPACING_AFTER);
	    
	    return table;
	}
	
	private PdfPTable generatePartnersInformation(String partnerInfo) {
		PdfPTable table = new PdfPTable(2);
		
		Config config = ConfigProvider.getInstance().getConfig();

		table.addCell(newBodyCell("Supplier (the developer)", true, true, true));
		table.addCell(newBodyCell("Partner (Google Play store)", true, true, true));
		table.addCell(newBodyCell(AccountingUtils.toCompanyInfo(config), false, true, true));
		table.addCell(newBodyCell(partnerInfo, false, true, true));
		
		table.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.setWidthPercentage(100f);
		table.setSpacingAfter(DEFAULT_SPACING_AFTER);
		
		return table;
	}
	
	private PdfPTable generateDistributorsReport(String info, Map<Country, List<Transaction>> transactions) throws DocumentException {
		if (transactions == null) {
			return null;
		}
		TransactionsDetails details = TransactionUtils.getDetails(transactions);
		if (details.getTotal() == 0) {
			return null;
		}
		return generateReportFor(details, info);
	}
	
	private PdfPTable generateTotalReport() throws DocumentException {
		TransactionsDetails details = TransactionUtils.getDetails(this.transactions);
		return generateReportFor(details, null);
	}
	
	private PdfPTable generateReportFor(TransactionsDetails details, String distributor) throws DocumentException {
		PdfPTable table = new PdfPTable(2);

		String cur = details.getCurrency();
		
		if (distributor != null && !distributor.isEmpty()) {
			table.addCell(newBodyCell("Distributor", true, true, true));
			table.addCell(newBodyCell(distributor, true, true, true));
		}
		Stream.of("Units sold", String.valueOf(details.getCharges(false)),
				"Charges", AccountingUtils.toAmountLine(details.getAmountCharges(false), cur),
				"Refunds", AccountingUtils.toAmountLine(details.getAmountChargeRefunds(), cur),
				"Google fees", AccountingUtils.toAmountLine(details.getAmonutGoogleFees(false), cur),
				"Google fees refunds", AccountingUtils.toAmountLine(details.getAmountGoogleFeeRefunds(), cur),
				"Taxes", AccountingUtils.toAmountLine(details.getAmountTaxes(false), cur),
				"Taxes refunds", AccountingUtils.toAmountLine(details.getAmountTaxRefunds(), cur))
		.forEach(content -> {
		    table.addCell(newBodyCell(content, false, true, true));
		});
		
		// Total charged (gross sales)
		table.addCell(newBodyCell("Gross sales (before taxes)", true, true, true));
		table.addCell(newBodyCell(AccountingUtils.toAmountLine(details.getGrossSales(), cur), true, true, true, Element.ALIGN_RIGHT));

		// Total taxes
		table.addCell(newBodyCell("Fees and taxes", true, true, true));
		table.addCell(newBodyCell(AccountingUtils.toAmountLine(details.getTotalFeesAndTaxes(), cur), true, true, true, Element.ALIGN_RIGHT));

		// Total paid to supplier
		table.addCell(newBodyCell("Total paid to supplier", true, true, true));
		table.addCell(newBodyCell(AccountingUtils.toAmountLine(details.getTotal(), cur), true, true, true, Element.ALIGN_RIGHT));
		
		table.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.setWidths(new float[] { 1, 2 });
		table.setWidthPercentage(100f);
		
		return table;
	}
	
	private Paragraph generateTotalSubtitle() {
		Paragraph paragraph = new Paragraph("Total sales", fontSubTitle);
		paragraph.setSpacingAfter(DEFAULT_SPACING_AFTER);
		return paragraph;
	}
	
	/* DISTRIBUTORS REPORT */
	
	/* MAIN REPORT */
	
	private void buildDistributorsReport(Document document,
			Map<Country.Distributor, Map<Country, List<Transaction>>> transactions) throws DocumentException {
		
		// Invoice header
		document.add(generatePageTitle("DETAILED EARNINGS PER DISTRIBUTORS"));
		document.add(generateInvoiceInformation());
		document.add(generatePartnersInformation(StaticConfig.DISTRIBUTOR_GOOGLE_COMMERCE_INFO));
		
		// Distributors reports
		document.add(generateSubtitle("Sales by Distributor"));
		for (Map.Entry<Country.Distributor, Map<Country, List<Transaction>>> entry: transactions.entrySet()) {
			PdfPTable dTable = generateDistributorsReport(
					entry.getKey().getLegalInfo(), entry.getValue());
			if (dTable != null) {
				document.add(dTable);
				if (entry.getKey().getVatInfo() != null) {
					document.add(generateCaption(entry.getKey().getVatInfo()));
				}
				document.add(Chunk.NEWLINE);
			}
		}
	}

	/* COUNTRIES REPORT */
	
	private void buildCountriesReport(Document document,
			Map<Country.Distributor, Map<Country, List<Transaction>>> transactions) throws DocumentException {
		
		// Invoice header
		document.add(generatePageTitle("DETAILED EARNINGS PER COUNTRY"));
		document.add(generateInvoiceInformation());
		document.add(generatePartnersInformation(StaticConfig.DISTRIBUTOR_GOOGLE_COMMERCE_INFO));

		// Countries report
		document.add(generateSubtitle("Sales per Country"));
		for (Map.Entry<Country.Distributor, Map<Country, List<Transaction>>> entry: transactions.entrySet()) {
			document.add(generateLabel(entry.getKey().getName()));
			document.add(generateCountriesReport(entry.getValue()));
			if (entry.getKey().getVatInfo() != null) {
				document.add(generateCaption(entry.getKey().getVatInfo()));
			}
		}
	}
	
	private PdfPTable generateCountriesReport(
			Map<Country, List<Transaction>> transactions) throws DocumentException {
		
		PdfPTable table = new PdfPTable(4);
		TransactionsDetails details;
		String cur = "";
		float totalAmount = 0f;
		
		table.addCell(newBodyCell("Country", true, true, true));
		table.addCell(newBodyCell("Units sold", true, true, true, Element.ALIGN_CENTER));
		table.addCell(newBodyCell("VAT status", true, true, true, Element.ALIGN_CENTER));
		table.addCell(newBodyCell("Amount", true, true, true, Element.ALIGN_RIGHT));

		// First add all states with beholden & partially paid VAT
		for (Map.Entry<Country, List<Transaction>> country: transactions.entrySet()) {
			if (country.getKey().getVatStatus() != VATStatus.FULLY_PAID) {
				details = TransactionUtils.getDetails(country.getValue());
				cur = details.getCurrency();
				appendDetailedCells(table, country.getKey().toString(),
						country.getKey().getVatStatus(), details, cur);
				
				totalAmount += details.getTotal();
			}
		}
		
		// Second add all states with paid VAT
		for (Map.Entry<Country, List<Transaction>> country: transactions.entrySet()) {
			if (country.getKey().getVatStatus() == VATStatus.FULLY_PAID) {
				details = TransactionUtils.getDetails(country.getValue());
				cur = details.getCurrency();
				appendDetailedCells(table, country.getKey().toString(),
						country.getKey().getVatStatus(), details, cur);
				
				totalAmount += details.getTotal();
			}
		}
		
		table.addCell(newBodyCell("Total paid to supplier", true, true, true));
		table.addCell(newBodyCell("", false, true, true));
		table.addCell(newBodyCell("", false, true, true));
		table.addCell(newBodyCell(AccountingUtils.toAmountLine(totalAmount, cur), true, true, true, Element.ALIGN_RIGHT));
		
		table.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.setWidths(new float[] { 35, 15, 25, 25 });
		table.setWidthPercentage(100f);
		table.setSpacingAfter(DEFAULT_SPACING_AFTER);
		
		return table;
	}
	
	/* USA REPORT */
	
	private void buildUSAReport(Document document, 
			Map<USAState, List<Transaction>> transactions) throws DocumentException {
		
		// Invoice header
		document.add(generatePageTitle("DETAILED EARNINGS FOR USA"));
		document.add(generateInvoiceInformation());
		document.add(generatePartnersInformation(StaticConfig.DISTRIBUTOR_GOOGLE_INC_INFO));
		
		// USA States reports
		document.add(generateSubtitle("Sales by State"));
		document.add(generateUsaStateReport(transactions));
		document.add(generateCaption(StaticConfig.VAT_INFO_USA));
	}
	
	private PdfPTable generateUsaStateReport(
			Map<USAState, List<Transaction>> transactions) throws DocumentException {
		
		PdfPTable table = new PdfPTable(4);
		TransactionsDetails details;
		String cur = "";
		float totalAmount = 0f;
		
		table.addCell(newBodyCell("State", true, true, true));
		table.addCell(newBodyCell("Units sold", true, true, true, Element.ALIGN_CENTER));
		table.addCell(newBodyCell("VAT status", true, true, true, Element.ALIGN_CENTER));
		table.addCell(newBodyCell("Amount", true, true, true, Element.ALIGN_RIGHT));

		// First add all states with exempted, beholden and partially paid VAT
		for (Map.Entry<USAState, List<Transaction>> state: transactions.entrySet()) {
			if (state.getKey().getVatStatus() != VATStatus.FULLY_PAID) {
				details = TransactionUtils.getDetails(state.getValue());
				cur = details.getCurrency();
				appendDetailedCells(table, state.getKey().toString(), 
						state.getKey().getVatStatus(), details, cur);
				
				totalAmount += details.getTotal();
			}
		}
		
		// Second add all states with paid VAT
		for (Map.Entry<USAState, List<Transaction>> state: transactions.entrySet()) {
			if (state.getKey().getVatStatus() == VATStatus.FULLY_PAID) {
				details = TransactionUtils.getDetails(state.getValue());
				cur = details.getCurrency();
				appendDetailedCells(table, state.getKey().toString(), 
						state.getKey().getVatStatus(), details, cur);
				
				totalAmount += details.getTotal();
			}
		}
		
		table.addCell(newBodyCell("Total paid to supplier", true, true, true));
		table.addCell(newBodyCell("", false, true, true));
		table.addCell(newBodyCell("", false, true, true));
		table.addCell(newBodyCell(AccountingUtils.toAmountLine(totalAmount, cur), true, true, true, Element.ALIGN_RIGHT));
		
		table.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.setWidths(new float[] { 35, 15, 25, 25 });
		table.setWidthPercentage(100f);
		table.setSpacingAfter(DEFAULT_SPACING_AFTER);
		
		return table;
	}
	
	/////////////////////
	// PRIVATE METHODS //
	/////////////////////

	private Document createDocument(String path) {
		Document document = new Document(PageSize.A4, 55, 55, 45, 45);
		
		try {
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));
			writer.setPageEvent(new PageNumeration(fontBody2));
			
			document.open();
			return document;
						
		} catch (FileNotFoundException e) {
			System.err.print("createDocument.file not found: " + StaticConfig.OUTPUT_FILE_NAME + ", " + e.getMessage() + "\n");
			
		} catch (DocumentException e) {
			System.err.print("createDocument.document error: " + e.getMessage() + "\n");
		}
		return null;
	}
	
	private PdfPCell newBodyCell(String content, boolean bold,
			boolean widhPaddingH, boolean withBorders) {
		return newBodyCell(content, bold, widhPaddingH, withBorders, Element.ALIGN_LEFT);
	}
	
	private PdfPCell newBodyCell(String content, boolean bold, 
			boolean widhPaddingH, boolean withBorders, int alignment) {
        PdfPCell cell = new PdfPCell();
        
        cell.setPhrase(new Phrase(content, bold ? fontBody1 : fontBody2));
        
        if (widhPaddingH) {
        	cell.setPadding(CELL_PADDING);
        } else {
        	cell.setPaddingLeft(CELL_PADDING);
        	cell.setPaddingRight(CELL_PADDING);
        }
        cell.setBorderWidth(withBorders ? 1 : 0);
        cell.setHorizontalAlignment(alignment);

        return cell;
	}
	
	private Paragraph generatePageTitle(String title) {
		Paragraph paragraph = new Paragraph(title, fontTitle);
		paragraph.setSpacingAfter(DEFAULT_SPACING_AFTER);
		return paragraph;
	}
	
	private Paragraph generateSubtitle(String subtitle) {
		Paragraph paragraph = new Paragraph(subtitle, fontSubTitle);
		paragraph.setSpacingAfter(DEFAULT_SPACING_AFTER);
		return paragraph;
	}
	
	private Paragraph generateLabel(String label) {
		Paragraph paragraph = new Paragraph(label, fontBody1);
		paragraph.setSpacingAfter(DEFAULT_SPACING_AFTER / 2);
		return paragraph;
	}
	
	private Paragraph generateCaption(String caption) {
		Paragraph paragraph = new Paragraph(caption, fontCaption);
		paragraph.setSpacingAfter(DEFAULT_SPACING_AFTER);
		return paragraph;
	}
	
	private void appendDetailedCells(PdfPTable table, String name, VATStatus vat, 
			TransactionsDetails details, String currency) {
		if (details.getTotal() != 0) {
			table.addCell(newBodyCell(
					name, false, true, true));
			table.addCell(newBodyCell(
					String.valueOf(details.getCharges(true)), false, true, true, Element.ALIGN_CENTER));
			table.addCell(newBodyCell(
					AccountingUtils.toVatStatusLine(vat), false, true, true, Element.ALIGN_CENTER));
			table.addCell(newBodyCell(
					AccountingUtils.toAmountLine(details.getTotal(), currency), false, true, true, Element.ALIGN_RIGHT));
		}
	}
	
}
