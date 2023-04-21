package com.gpea.generator;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

class PageNumeration extends PdfPageEventHelper {

	private Font font;
	
	private PdfTemplate total;
	
	PageNumeration(Font font) {
		this.font = font;
	}

	/**
	 * Creates the PdfTemplate that will hold the total number of pages.
	 * 
	 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(
	 *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
	 */
	public void onOpenDocument(PdfWriter writer, Document document) {
		total = writer.getDirectContent().createTemplate(30, 12);
	}

	/**
	 * Adds a header to every page
	 * 
	 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(
	 *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
	 */
	public void onEndPage(PdfWriter writer, Document document) {
		PdfPTable table = new PdfPTable(3);
		try {
			table.setWidths(new int[] { 24, 24, 2 });
			table.getDefaultCell().setFixedHeight(10);
			table.getDefaultCell().setBorder(Rectangle.TOP);
			PdfPCell cell = new PdfPCell();
			cell.setBorder(0);
			cell.setBorderWidthTop(1);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPhrase(new Phrase("Google Play Earnings Receipt", font));
			table.addCell(cell);

			cell = new PdfPCell();
			cell.setBorder(0);
			cell.setBorderWidthTop(1);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setPhrase(new Phrase(String.format("Page %d of", writer.getPageNumber()), font));
			table.addCell(cell);

			cell = new PdfPCell(Image.getInstance(total));
			cell.setBorder(0);
			cell.setBorderWidthTop(1);
			table.addCell(cell);
			table.setTotalWidth(document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin());
			table.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin() - 15,
					writer.getDirectContent());
			
		} catch (DocumentException de) {
			throw new ExceptionConverter(de);
		}
	}

	/**
	 * Fills out the total number of pages before the document is closed.
	 * 
	 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onCloseDocument(
	 *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
	 */
	public void onCloseDocument(PdfWriter writer, Document document) {
		ColumnText.showTextAligned(total, Element.ALIGN_LEFT,
				new Phrase(String.valueOf(writer.getPageNumber()), font), 2, 0, 0);
	}
	
}
