package com.gek.and.project4.async;

import com.gek.and.project4.entity.Booking;

import java.util.List;

/**
 * Created by moo on 23.03.18.
 */

public class ImportResult {
	private ImportResultType result;
	private String message;
	private List<Booking> imports;

	public ImportResult(ImportResultType resultType) {
		this.result = resultType;
	}

	public ImportResultType getResult() {
		return result;
	}

	public void setResult(ImportResultType result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Booking> getImports() {
		return imports;
	}

	public void setImports(List<Booking> imports) {
		this.imports = imports;
	}

	public enum ImportResultType {
		DONE(), FAILED();
	}
}
