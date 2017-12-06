package com.gek.and.project4.types;


public enum PeriodType {
	TODAY(0),
	WEEK(1),
	MONTH(2),
	YEAR(3),
	PRIOR_YEAR(4),
	PRIOR_MONTH(5);
	
	private int code;
	
	PeriodType(int code) {
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}
	
	public static PeriodType fromInt(int code) {
		if (code == 0) return PeriodType.TODAY;
		if (code == 1) return PeriodType.WEEK;
		if (code == 2) return PeriodType.MONTH;
		if (code == 3) return PeriodType.YEAR;
		if (code == 4) return PeriodType.PRIOR_YEAR;
		if (code == 5) return PeriodType.PRIOR_MONTH;

		throw new IllegalArgumentException("Illegal PeriodType with code " + code);
	}
}
