package com.fenoreste.controller;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fenoreste.dto.BackendOperationResultDTO;
import com.fenoreste.dto.Bank;
import com.fenoreste.dto.ThirdPartyProductDTO;
import com.fenoreste.dto.UserDocumentIdDTO;
import com.fenoreste.service.IOtrosService;
import com.fenoreste.service.TerceroServiceSpring;
import com.github.cliftonlabs.json_simple.JsonObject;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping({ "/Third" })
public class ThirdController {

	@Autowired
	TerceroServiceSpring tercerosService;
	
	@Autowired
	IOtrosService otrosService;

	@PostMapping(value = "/Party/Product/Validate", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity thirdValidate(@RequestBody String cadena) {
		
		System.out.println("Entro a productValidate");
		if(!otrosService.servicios_activos()) {
			JsonObject json_horario=new JsonObject();
			json_horario.put("Error", "VERIFIQUE SU HORARIO DE ACTIVIDAD FECHA,HORA O CONTACTE A SU PROVEEEDOR");
			return ResponseEntity.status(400).body(json_horario);
		}
		
		ArrayList<String> clientBankIdentifiers_ = new ArrayList<>();
		String thirdPartyProductNumber_;
		String thirdPartyProductBankIdentifier_;
		String alias_;
		String currencyId_;
		Integer transactionSubType_;
		Integer thirdPartyProductType_;
		Integer productType_;
		String ownerName_;
		String ownerCountryId_;
		String ownerEmail_;
		String ownerCity_;
		String ownerAddress_;
		UserDocumentIdDTO ownerDocumentId_ = new UserDocumentIdDTO();
		String ownerPhoneNumber_;
		Bank bank_ = new Bank();
		Bank correspondentBank_ = new Bank();
		UserDocumentIdDTO userDocumentId_ = new UserDocumentIdDTO();
		ThirdPartyProductDTO dtoTercero = new ThirdPartyProductDTO();
		try {
			JSONObject request = new JSONObject(cadena);
			// Obtenemos la lista de clientes en el json
			JSONArray clientBankIdentifiers = request.getJSONArray("clientBankIdentifiers");
			for (int i = 0; i < clientBankIdentifiers.length(); i++) {
				clientBankIdentifiers_.add(clientBankIdentifiers.getString(i));
			}
			thirdPartyProductNumber_ = request.getString("thirdPartyProductNumber");
			thirdPartyProductBankIdentifier_ = request.getString("thirdPartyProductBankIdentifier");
			alias_ = request.getString("alias");
			currencyId_ = request.getString("currencyId");
			transactionSubType_ = request.getInt("transactionSubType");
			thirdPartyProductType_ = request.getInt("thirdPartyProductType");
			productType_ = request.getInt("productType");
			ownerName_ = request.getString("ownerName");
			ownerCountryId_ = request.getString("ownerCountryId");
			ownerEmail_ = request.getString("ownerEmail");
			ownerCity_ = request.getString("ownerCity");
			ownerAddress_ = request.getString("ownerAddress");
			ownerPhoneNumber_ = request.getString("ownerPhoneNumber");

			// Obtenemos el objeto userDocumentId para ownerDocumentId
			JSONObject ownerDocumentId = request.getJSONObject("ownerDocumentId");
			ownerDocumentId_.setDocumentNumber(ownerDocumentId.getInt("documentNumber"));
			ownerDocumentId_.setDocumentType(ownerDocumentId.getInt("documentType"));
			ownerDocumentId_.setIntegrationProperties(ownerDocumentId.getString("integrationProperties"));

			// Obtenemos el objeto Bank para bank
			JSONObject bank = request.getJSONObject("bank");
			bank_.setBankId(bank.getInt("bankId"));
			bank_.setCountryId(bank.getString("countryId"));
			bank_.setDescription(bank.getString("description"));
			bank_.setHeadQuartersAddress(bank.getString("headQuartersAddress"));
			bank_.setRoutingCode(bank.getString("routingCode"));

			// Obtenemos el objeto Bank para correspondentBank
			JSONObject correspondentBank = request.getJSONObject("correspondentBank");
			correspondentBank_.setBankId(correspondentBank.getInt("bankId"));
			correspondentBank_.setCountryId(correspondentBank.getString("countryId"));
			correspondentBank_.setDescription(correspondentBank.getString("description"));
			correspondentBank_.setHeadQuartersAddress(correspondentBank.getString("headQuartersAddress"));
			correspondentBank_.setRoutingCode(correspondentBank.getString("routingCode"));

			// Obtenemos el objeto userDocumentId para ownerDocumentId
			JSONObject userDocumentId = request.getJSONObject("userDocumentId");
			userDocumentId_.setDocumentNumber(userDocumentId.getInt("documentNumber"));
			userDocumentId_.setDocumentType(userDocumentId.getInt("documentType"));
			userDocumentId_.setIntegrationProperties(userDocumentId.getString("integrationProperties"));

			dtoTercero.setClientBankIdentifiers(clientBankIdentifiers_);
			dtoTercero.setThirdPartyProductNumber(thirdPartyProductNumber_);
			dtoTercero.setThirdPartyProductBankIdentifier(thirdPartyProductBankIdentifier_);
			dtoTercero.setAlias(alias_);
			dtoTercero.setCurrencyId(currencyId_);
			dtoTercero.setTransactionSubType(transactionSubType_);
			dtoTercero.setOwnerName(ownerName_);
			dtoTercero.setOwnerCountryId(ownerCountryId_);
			dtoTercero.setOwnerEmail(ownerEmail_);
			dtoTercero.setOwnerCity(ownerCity_);
			dtoTercero.setOwnerCity(ownerCity_);
			dtoTercero.setOwnerAddress(ownerAddress_);
			dtoTercero.setOwnerDocumentId(ownerDocumentId_);
			dtoTercero.setOwnerPhoneNumber(ownerPhoneNumber_);
			dtoTercero.setBank(bank_);
			dtoTercero.setCorrespondentBank(correspondentBank_);
			dtoTercero.setUserDocumentId(userDocumentId_);
			dtoTercero.setThirdPartyProductType(thirdPartyProductType_);
			dtoTercero.setProductType(productType_);
		} catch (Exception e) {
			return ResponseEntity.status(500).body(e.getMessage());
		}
		JsonObject jsonR = new JsonObject();
		BackendOperationResultDTO response = tercerosService.validarProductoTerceros(dtoTercero);
		JsonObject jsonResponse = new JsonObject();
		jsonResponse.put("BackendOperationResult", response);
		return ResponseEntity.status(200).body(jsonResponse);
	}

	@PostMapping(value = "/Party/Account/ProductOwnerAndCurrency", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity altaTerceros(@RequestBody String cadena) {	
		System.out.println("Entro a productOwnerAndCurrency");
		if(!otrosService.servicios_activos()) {
			JsonObject json_horario=new JsonObject();
			json_horario.put("Error", "VERIFIQUE SU HORARIO DE ACTIVIDAD FECHA,HORA O CONTACTE A SU PROVEEEDOR");
			return ResponseEntity.status(400).body(json_horario);
		}

		JsonObject third = new JsonObject();
		
		try {
			JSONObject jsonRequest = new JSONObject(cadena);
			String productNumber_ = "";
			Integer productTypeId_ = 0;
			Integer thirdPartyProductType_ = 0;
			// validamos que nuestro request este bien formado
			productNumber_ = jsonRequest.getString("productNumber");
			productTypeId_ = jsonRequest.getInt("productTypeId");
			// Otengo el objeto de productOwnerDocumentId
			JSONObject productOwnerDocumentId = jsonRequest.getJSONObject("productOwnerDocumentId");
			String documentNumber_ = productOwnerDocumentId.getString("documentNumber");
			String documentType_ = productOwnerDocumentId.getString("documentType");
			thirdPartyProductType_ = jsonRequest.getInt("thirdPartyProductType");
		

		UserDocumentIdDTO documento = new UserDocumentIdDTO();

		ThirdPartyProductDTO dto = tercerosService.cosultaProductosTerceros(productNumber_, productTypeId_, documento,
				thirdPartyProductType_);
		third.put("productOwnerAndCurrency", dto);
		} catch (Exception e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
		return ResponseEntity.status(200).body(third);

	}

}
