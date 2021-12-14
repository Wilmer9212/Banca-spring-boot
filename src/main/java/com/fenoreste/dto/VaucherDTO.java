package com.fenoreste.dto;

import java.util.Arrays;

public class VaucherDTO {

	byte[] productBankStatementFile;
	String productBankStatementFileName;
	
	public VaucherDTO() {
		// TODO Auto-generated constructor stub
	}

	public byte[] getProductBankStatementFile() {
		return productBankStatementFile;
	}

	public void setProductBankStatementFile(byte[] productBankStatementFile) {
		this.productBankStatementFile = productBankStatementFile;
	}

	public String getProductBankStatementFileName() {
		return productBankStatementFileName;
	}

	public void setProductBankStatementFileName(String productBankStatementFileName) {
		this.productBankStatementFileName = productBankStatementFileName;
	}

	
}
