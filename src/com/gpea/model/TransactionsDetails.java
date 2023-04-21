package com.gpea.model;

public class TransactionsDetails {
	
	private String currency;
	
	private int charges;
	private int chargeRefunds;
	private float amountCharges;
	private float amountChargeRefunds;
	
	private int googleFees;
	private int googleFeeRedunds;
	private float amountGoogleFees;
	private float amountGoogleFeeRefunds;
	
	private int taxes;
	private int taxRefunds;
	private float amountTaxes;
	private float amountTaxRefunds;
	
	public TransactionsDetails(String currency) {
		this.currency = currency;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public int getCharges(boolean withRefunds) {
		return withRefunds ? charges - chargeRefunds : charges;
	}
	public void setCharges(int charges) {
		this.charges = charges;
	}
	
	public int getChargeRefunds() {
		return chargeRefunds;
	}
	public void setChargeRefunds(int chargeRefunds) {
		this.chargeRefunds = chargeRefunds;
	}
	
	public float getAmountCharges(boolean withRefunds) {
		return withRefunds ? amountCharges + amountChargeRefunds : amountCharges;
	}
	public void setAmountCharges(float amountCharges) {
		this.amountCharges = amountCharges;
	}
	
	public float getAmountChargeRefunds() {
		return amountChargeRefunds;
	}
	public void setAmountChargeRefunds(float amountChargeRefunds) {
		this.amountChargeRefunds = amountChargeRefunds;
	}
	
	public int getGoogleFees(boolean withRefunds) {
		return withRefunds ? googleFees - googleFeeRedunds : googleFees;
	}
	public void setGoogleFees(int googleFees) {
		this.googleFees = googleFees;
	}
	
	public int getGoogleFeeRedunds() {
		return googleFeeRedunds;
	}
	public void setGoogleFeeRedunds(int googleFeeRedunds) {
		this.googleFeeRedunds = googleFeeRedunds;
	}
	
	public float getAmonutGoogleFees(boolean withRefunds) {
		return withRefunds ? amountGoogleFees + amountGoogleFeeRefunds : amountGoogleFees;
	}
	public void setAmountGoogleFees(float amountGoogleFees) {
		this.amountGoogleFees = amountGoogleFees;
	}
	
	public float getAmountGoogleFeeRefunds() {
		return amountGoogleFeeRefunds;
	}
	public void setAmountGoogleFeeRefunds(float amountGoogleFeeRedunds) {
		this.amountGoogleFeeRefunds = amountGoogleFeeRedunds;
	}
	
	public int getTaxes(boolean withRefunds) {
		return withRefunds ? taxes - taxRefunds : taxes;
	}
	public void setTaxes(int taxes) {
		this.taxes = taxes;
	}
	
	public int getTaxRefunds() {
		return taxRefunds;
	}
	public void setTaxRefunds(int taxRefunds) {
		this.taxRefunds = taxRefunds;
	}
	
	public float getAmountTaxes(boolean withRefunds) {
		return withRefunds ? amountTaxes + amountTaxRefunds : amountTaxes;
	}
	public void setAmountTaxes(float amountTaxes) {
		this.amountTaxes = amountTaxes;
	}
	
	public float getAmountTaxRefunds() {
		return amountTaxRefunds;
	}
	public void setAmountTaxRefunds(float amountTaxRefunds) {
		this.amountTaxRefunds = amountTaxRefunds;
	}
	
	public float getTotal() {
		return amountCharges + amountChargeRefunds +
				amountGoogleFees + amountGoogleFeeRefunds +
				amountTaxes + amountTaxRefunds;
	}
	public float getGrossSales() {
		return amountCharges + amountChargeRefunds;
	}
	public float getTotalFeesAndTaxes() {
		return amountGoogleFees + amountGoogleFeeRefunds +
				amountTaxes + amountTaxRefunds;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		if (getCharges(true) > 1) {
			sb.append(getCharges(true)).append(" sales ");
		} else {
			sb.append(getCharges(true)).append(" sale ");
		}
		
		if (getChargeRefunds() > 0) {
			if (getChargeRefunds() > 1) {
				sb.append(" (").append(getChargeRefunds()).append(" refunds) ");
			} else {
				sb.append(" (").append(getChargeRefunds()).append(" refund) ");
			}
		}
		
		sb.append(String.format("-> %.2f %s ", getTotal(), currency));
		
		if (getTotalFeesAndTaxes() < 0) {
			sb.append(String.format("(incl.fees+taxes -> %.2f %s)", getTotalFeesAndTaxes(), currency));
		}
		
		return sb.toString();
	}

}
