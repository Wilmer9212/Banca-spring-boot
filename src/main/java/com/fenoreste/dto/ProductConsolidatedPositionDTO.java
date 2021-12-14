package com.fenoreste.dto;

import java.math.BigDecimal;

public class ProductConsolidatedPositionDTO {

	   private String clientBankIdentifier;
	      private String productBankIdentifier;
	      private String productTypeId;
	      private String productAlias;
	      private String productNumber;
	      private Integer localCurrencyId;
	      private Double localBalance;
	      private Integer internationalCurrencyId;
	      private Double internationalBalance;
	      private Double rate;
	      private String expirationDate;
	      private Integer paidFees;
	      private Integer term;
	      private String nextFeeDueDate;
	      private String productOwnerName;
	      private String productBranchName;
	      private Integer canTransact;
	      private Integer subsidiaryId;
	      private String subsidiaryName;
	      private Integer backendId;

	    public ProductConsolidatedPositionDTO() {  }
	    
	    
	    public String getClientBankIdentifier() {
	        return clientBankIdentifier;
	    }

	    public void setClientBankIdentifier(String clientBankIdentifier) {
	        this.clientBankIdentifier = clientBankIdentifier;
	    }

	    public String getProductBankIdentifier() {
	        return productBankIdentifier;
	    }

	    public void setProductBankIdentifier(String productBankIdentifier) {
	        this.productBankIdentifier = productBankIdentifier;
	    }

	    public String getProductTypeId() {
	        return productTypeId;
	    }

	    public void setProductTypeId(String productTypeId) {
	        this.productTypeId = productTypeId;
	    }

	    public String getProductAlias() {
	        return productAlias;
	    }

	    public void setProductAlias(String productAlias) {
	        this.productAlias = productAlias;
	    }

	    public String getProductNumber() {
	        return productNumber;
	    }

	    public void setProductNumber(String productNumber) {
	        this.productNumber = productNumber;
	    }

	    public Integer getLocalCurrencyId() {
	        return localCurrencyId;
	    }

	    public void setLocalCurrencyId(Integer localCurrencyId) {
	        this.localCurrencyId = localCurrencyId;
	    }

	    public Double getLocalBalance() {
	        return localBalance;
	    }

	    public void setLocalBalance(Double localBalance) {
	        this.localBalance = localBalance;
	    }

	    public Integer getInternationalCurrencyId() {
	        return internationalCurrencyId;
	    }

	    public void setInternationalCurrencyId(Integer internationalCurrencyId) {
	        this.internationalCurrencyId = internationalCurrencyId;
	    }

	    public Double getInternationalBalance() {
	        return internationalBalance;
	    }

	    public void setInternationalBalance(Double internationalBalance) {
	        this.internationalBalance = internationalBalance;
	    }

	    public Double getRate() {
	        return rate;
	    }

	    public void setRate(Double rate) {
	        this.rate = rate;
	    }

	    public String getExpirationDate() {
	        return expirationDate;
	    }

	    public void setExpirationDate(String expirationDate) {
	        this.expirationDate = expirationDate;
	    }

	    public Integer getPaidFees() {
	        return paidFees;
	    }

	    public void setPaidFees(Integer paidFees) {
	        this.paidFees = paidFees;
	    }

	    public Integer getTerm() {
	        return term;
	    }

	    public void setTerm(Integer term) {
	        this.term = term;
	    }

	    public String getNextFeeDueDate() {
	        return nextFeeDueDate;
	    }

	    public void setNextFeeDueDate(String nextFeeDueDate) {
	        this.nextFeeDueDate = nextFeeDueDate;
	    }

	    public String getProductOwnerName() {
	        return productOwnerName;
	    }

	    public void setProductOwnerName(String productOwnerName) {
	        this.productOwnerName = productOwnerName;
	    }

	    public String getProductBranchName() {
	        return productBranchName;
	    }

	    public void setProductBranchName(String productBranchName) {
	        this.productBranchName = productBranchName;
	    }

	    public Integer getCanTransact() {
	        return canTransact;
	    }

	    public void setCanTransact(Integer canTransact) {
	        this.canTransact = canTransact;
	    }

	    public Integer getSubsidiaryId() {
	        return subsidiaryId;
	    }

	    public void setSubsidiaryId(Integer subsidiaryId) {
	        this.subsidiaryId = subsidiaryId;
	    }

	    public String getSubsidiaryName() {
	        return subsidiaryName;
	    }

	    public void setSubsidiaryName(String subsidiaryName) {
	        this.subsidiaryName = subsidiaryName;
	    }

	    public Integer getBackendId() {
	        return backendId;
	    }

	    public void setBackendId(Integer backendId) {
	        this.backendId = backendId;
	    }

	    @Override
	    public String toString() {
	        return "ProductsConsolidatePositionDTO{" + "clientBankIdentifier=" + clientBankIdentifier + ", productBankIdentifier=" + productBankIdentifier + ", productTypeId=" + productTypeId + ", productAlias=" + productAlias + ", productNumber=" + productNumber + ", localCurrencyId=" + localCurrencyId + ", localBalance=" + localBalance + ", internationalCurrencyId=" + internationalCurrencyId + ", internationalBalance=" + internationalBalance + ", rate=" + rate + ", expirationDate=" + expirationDate + ", paidFees=" + paidFees + ", term=" + term + ", nextFeeDueDate=" + nextFeeDueDate + ", productOwnerName=" + productOwnerName + ", productBranchName=" + productBranchName + ", canTransact=" + canTransact + ", subsidiaryId=" + subsidiaryId + ", subsidiaryName=" + subsidiaryName + ", backendId=" + backendId + '}';
	    }

	    
}
