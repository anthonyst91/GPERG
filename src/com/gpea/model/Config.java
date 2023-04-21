package com.gpea.model;

public class Config {

	private String companyName;
	private String street;
	private String zipcode;
	private String city;
	private String country;
	private String vatNumber;
	
	private String salesPerdiod;
	private String invoiceNumber;
	private String invoiceDate;
	private String paymentDate;
	
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getVatNumber() {
		return vatNumber;
	}
	public void setVatNumber(String vatNumber) {
		this.vatNumber = vatNumber;
	}
	
	public String getSalesPerdiod() {
		return salesPerdiod;
	}
	public void setSalesPerdiod(String salesPerdiod) {
		this.salesPerdiod = salesPerdiod;
	}
	
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	
	public String getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	
	public String getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}
	@Override
	public String toString() {
		return "Config [companyName=" + companyName + ", street=" + street + ", zipcode=" + zipcode + ", city=" + city
				+ ", country=" + country + ", vatNumber=" + vatNumber + ", salesPerdiod=" + salesPerdiod
				+ ", invoiceNumber=" + invoiceNumber + ", invoiceDate=" + invoiceDate + ", paymentDate=" + paymentDate
				+ "]";
	}

}

