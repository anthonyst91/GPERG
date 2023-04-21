package com.gpea.model;

import com.gpea.util.CountryProvider;

public class Transaction {
	
	public enum Type {
		CHARGE, CHARGE_REFUND,
		FEE, FEE_REFUND,
		TAX, TAX_REFUND,
		UNKNOWN
	}
	
	public enum ProductType {
		PURCHASE, IN_APP, UNKNOWN
	}
	
	private String id; 					// The transaction ID starting with GPA.
	private Type type;            		// The transaction type (Charge, Charge refund, Google fee, Google fee refund, Tax, Tax refund)
	private String date; 				// The date of the transaction (format: Dec 1, 2019)
	private String time; 				// The time of the transaction (format: 1:59:55 AM PST)
	private String taxType; 			// The tax type (can be empty)
	private String refundType;      	// The refund type (Full, Partial, or empty)
	
	private String productTitle;		// The product name
	private String productId;			// The package name
	private ProductType productType;	// The product type (0 for app purchase, 1 for in-app purchase
	private String skuId;				// The sku id of the in-app purchase (can be empty)
	
	private String buyerHardware;		// Hardware of the buyer
	private Country buyerCountry;		// Country of the buyer
	private String buyerState;			// State of the buyer (can be empty)
	private String buyerPostalCode;		// Postal code of the buyer
	
	private float buyerAmount;			// The amount paid by the user (in its own currency)
	private String buyerCurrency;		// Currency of the buyer
	private float conversionRate;		// The rate applied for the currency conversion
	private float merchantAmount;		// The amount paid by the user (in the merchant currency)
	private String merchantCurrency;	// The merchant currency

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public Type getType() {
		return type;
	}
	public void setType(String type) {
		switch (type) {
			case "Charge": this.type = Type.CHARGE; break;
			case "Charge refund": this.type = Type.CHARGE_REFUND; break;
			case "Google fee": this.type = Type.FEE; break;
			case "Google fee refund": this.type = Type.FEE_REFUND; break;
			case "Tax": this.type = Type.TAX; break;
			case "Tax refund": this.type = Type.TAX_REFUND; break;
			default: this.type = Type.UNKNOWN; break;
		}
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	public String getTaxType() {
		return taxType;
	}
	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}
	
	public String getRefundType() {
		return refundType;
	}
	public void setRefundType(String refundType) {
		this.refundType = refundType;
	}
	
	public String getProductTitle() {
		return productTitle;
	}
	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}
	
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public ProductType getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		switch (productType) {
			case "0": this.productType = ProductType.PURCHASE; break;
			case "1": this.productType = ProductType.IN_APP; break;
			default: this.productType = ProductType.UNKNOWN; break;
		}
	}
	
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	
	public String getBuyerHardware() {
		return buyerHardware;
	}
	public void setBuyerHardware(String buyerHardware) {
		this.buyerHardware = buyerHardware;
	}
	
	public Country getBuyerCountry() {
		return buyerCountry;
	}
	public void setBuyerCountry(String buyerCountry) {
		this.buyerCountry = CountryProvider.getInstance().getCountryByIsoCode(buyerCountry);
	}
	
	public String getBuyerState() {
		return buyerState;
	}
	public void setBuyerState(String buyerState) {
		this.buyerState = buyerState;
	}
	
	public String getBuyerPostalCode() {
		return buyerPostalCode;
	}
	public void setBuyerPostalCode(String buyerPostalCode) {
		this.buyerPostalCode = buyerPostalCode;
	}
	
	public float getBuyerAmount() {
		return buyerAmount;
	}
	public void setBuyerAmount(String buyerAmount) {
		this.buyerAmount = Float.parseFloat(buyerAmount);
	}
	
	public String getBuyerCurrency() {
		return buyerCurrency;
	}
	public void setBuyerCurrency(String buyerCurrency) {
		this.buyerCurrency = buyerCurrency;
	}
	
	public float getConversionRate() {
		return conversionRate;
	}
	public void setConversionRate(String conversionRate) {
		this.conversionRate = Float.parseFloat(conversionRate);
	}
	
	public float getMerchantAmount() {
		return merchantAmount;
	}
	public void setMerchantAmount(String merchantAmount) {
		this.merchantAmount = Float.parseFloat(merchantAmount);
	}
	
	public String getMerchantCurrency() {
		return merchantCurrency;
	}
	public void setMerchantCurrency(String merchantCurrency) {
		this.merchantCurrency = merchantCurrency;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(id).append(" [").append(type.name()).append("]: ");
		sb.append(buyerCountry);
		if (buyerState != null && !buyerState.isEmpty()) sb.append(" (").append(buyerState).append(")");
		sb.append(" | ");
		sb.append(buyerAmount).append(" ").append(buyerCurrency);
		sb.append(" | ");
		sb.append(merchantAmount).append(" ").append(merchantCurrency);
		
		return sb.toString();
	}
	
}
