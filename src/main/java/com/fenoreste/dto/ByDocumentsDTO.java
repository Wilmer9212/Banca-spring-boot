package com.fenoreste.dto;

public class ByDocumentsDTO {

    private String ClientBankIdentifier;
    private String ClientName;
    private String ClientType;
    private String DocumentId;

    public ByDocumentsDTO() {
		// TODO Auto-generated constructor stub
	}

	public String getClientBankIdentifier() {
		return ClientBankIdentifier;
	}

	public void setClientBankIdentifier(String clientBankIdentifier) {
		ClientBankIdentifier = clientBankIdentifier;
	}

	public String getClientName() {
		return ClientName;
	}

	public void setClientName(String clientName) {
		ClientName = clientName;
	}

	public String getClientType() {
		return ClientType;
	}

	public void setClientType(String clientType) {
		ClientType = clientType;
	}

	public String getDocumentId() {
		return DocumentId;
	}

	public void setDocumentId(String documentId) {
		DocumentId = documentId;
	}

	@Override
	public String toString() {
		return "ByDocumentsDTO [ClientBankIdentifier=" + ClientBankIdentifier + ", ClientName=" + ClientName
				+ ", ClientType=" + ClientType + ", DocumentId=" + DocumentId + "]";
	}
   
}
