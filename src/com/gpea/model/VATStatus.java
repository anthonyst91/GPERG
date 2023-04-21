package com.gpea.model;

public enum VATStatus {
	
	FULLY_PAID, PARTIALLY_PAID, BEHOLDEN, EXEMPTED;
	
	public static VATStatus getBy(String vatStatus) {
		if (vatStatus != null && !vatStatus.isEmpty()) {
			switch (vatStatus) {
				case "1": return VATStatus.FULLY_PAID;
				case "2": return VATStatus.PARTIALLY_PAID;
				case "3": return VATStatus.EXEMPTED;
				default: return VATStatus.BEHOLDEN;
			}
		} else {
			return VATStatus.BEHOLDEN;
		}
	}

}
