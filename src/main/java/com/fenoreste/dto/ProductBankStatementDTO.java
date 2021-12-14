package com.fenoreste.dto;

public class ProductBankStatementDTO {


    private String productBankIdentifier;
    private String productBankStatementDate;
    private String ProductBankStatementId;
    private int ProductType;

    public ProductBankStatementDTO() {
    }

    public ProductBankStatementDTO(String productBankIdentifier, String productBankStatementDate, String ProductBankStatementId, int ProductType) {
        this.productBankIdentifier = productBankIdentifier;
        this.productBankStatementDate = productBankStatementDate;
        this.ProductBankStatementId = ProductBankStatementId;
        this.ProductType = ProductType;
    }

    public String getProductBankIdentifier() {
        return productBankIdentifier;
    }

    public void setProductBankIdentifier(String productBankIdentifier) {
        this.productBankIdentifier = productBankIdentifier;
    }

    public String getProductBankStatementDate() {
        return productBankStatementDate;
    }

    public void setProductBankStatementDate(String productBankStatementDate) {
        this.productBankStatementDate = productBankStatementDate;
    }

    public String getProductBankStatementId() {
        return ProductBankStatementId;
    }

    public void setProductBankStatementId(String ProductBankStatementId) {
        this.ProductBankStatementId = ProductBankStatementId;
    }

    public int getProductType() {
        return ProductType;
    }

    public void setProductType(int ProductType) {
        this.ProductType = ProductType;
    }

    @Override
    public String toString() {
        return "ProductBankStatementDTO{" + "productBankIdentifier=" + productBankIdentifier + ", productBankStatementDate=" + productBankStatementDate + ", ProductBankStatementId=" + ProductBankStatementId + ", ProductType=" + ProductType + '}';
    }

    
}
