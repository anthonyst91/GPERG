package com.gpea.model;

import com.gpea.StaticConfig;

public class Country {
	
	public static final String USA_CODE = "US";
	
	public enum Distributor  {
		GOOGLE_COMMERCE_LTD("Google Commerce Ltd", 
				StaticConfig.DISTRIBUTOR_GOOGLE_COMMERCE_INFO, 
				StaticConfig.VAT_INFO_EUROPA),
		GOOGLE_INC ("Google Inc.", 
				StaticConfig.DISTRIBUTOR_GOOGLE_INC_INFO, 
				null),
		GOOGLE_ASIA_PACIFIC_PTE_LTD("Google Asia Pacific Pte. Ltd", 
				StaticConfig.DISTRIBUTOR_GOOGLE_ASIA_PACIFIC_INFO, 
				null);
		
		private String name;
		private String legalInfo;
		private String vatInfo;
		Distributor(String name, String legalInfo, String vatInfo) {
			this.name = name;
			this.legalInfo = legalInfo;
			this.vatInfo = vatInfo;
		}
		public String getName() {
			return name;
		}
		public String getLegalInfo() {
			return legalInfo;
		}
		public String getVatInfo() {
			return vatInfo;
		}
	}
	
	public enum Region {
		ASIA, AFRICA, EUROPA, 
		AMERICAS, OCEANIA, ANTARTICA
	}
	
	private String name;
	private String code;
	private Region region;
	private Distributor distributor;
	private String subRegion;
	private String intermediateRegion;
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
	
	public Region getRegion() {
		return region;
	}
	public void setRegion(String region) {
		switch (region) {
			case "Asia": 
				this.region = Region.ASIA;
				this.distributor = Distributor.GOOGLE_ASIA_PACIFIC_PTE_LTD;
				break;
			case "Europe": 
				this.region = Region.EUROPA;
				this.distributor = Distributor.GOOGLE_COMMERCE_LTD;
				break;
			case "Africa": 
				this.region = Region.AFRICA;
				this.distributor = Distributor.GOOGLE_COMMERCE_LTD;
				break;
			case "Americas": 
				this.region = Region.AMERICAS;
				this.distributor = Distributor.GOOGLE_INC;
				break;
			case "Oceania": 
				this.region = Region.OCEANIA;
				this.distributor = Distributor.GOOGLE_ASIA_PACIFIC_PTE_LTD;
				break;
			case "Antartica": 
				this.region = Region.ANTARTICA; 
				this.distributor = Distributor.GOOGLE_INC;
				break;
		};
	}
	
	public Distributor getDistributor() {
		return distributor;
	}
	
	public String getSubRegion() {
		return subRegion;
	}
	public void setSubRegion(String subRegion) {
		this.subRegion = subRegion;
	}
	
	public String getIntermediateRegion() {
		return intermediateRegion;
	}
	public void setIntermediateRegion(String intermediateRegion) {
		this.intermediateRegion = intermediateRegion;
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
