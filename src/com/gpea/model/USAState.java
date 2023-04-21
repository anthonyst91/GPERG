package com.gpea.model;

public class USAState {
	
	private String name;
	private String code;
	private VATStatus vatStatus;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public VATStatus getVatStatus() {
		return vatStatus;
	}
	public void setVatStatus(String vatStatus) {
		this.vatStatus = VATStatus.getBy(vatStatus);
	}
	
	@Override
	public String toString() {
		return String.format("%s [%s]", name, code);
	}

}
