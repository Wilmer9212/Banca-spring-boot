package com.fenoreste.dto;

public class UserDocumentIdDTO {

    private String integrationProperties;
    private Integer documentNumber;
    private Integer documentType;

    public UserDocumentIdDTO() {
    }

    public UserDocumentIdDTO(String integrationProperties, Integer documentNumber, Integer documentType) {
        this.integrationProperties = integrationProperties;
        this.documentNumber = documentNumber;
        this.documentType = documentType;
    }

    public String getIntegrationProperties() {
        return integrationProperties;
    }

    public void setIntegrationProperties(String integrationProperties) {
        this.integrationProperties = integrationProperties;
    }

    public Integer getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(Integer documentNumber) {
        this.documentNumber = documentNumber;
    }

    public Integer getDocumentType() {
        return documentType;
    }

    public void setDocumentType(Integer documentType) {
        this.documentType = documentType;
    }

        
}
