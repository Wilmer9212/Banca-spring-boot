package com.fenoreste.controller;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fenoreste.dto.BackendOperationResultDTO;
import com.fenoreste.dto.DocumentIdTransaccionesDTO;
import com.fenoreste.dto.RequestDataOrdenPagoDTO;
import com.fenoreste.dto.TransactionToOwnAccountsDTO;
import com.fenoreste.dto.VaucherDTO;
import com.fenoreste.service.IAuxiliares_dService;
import com.fenoreste.service.IOtrosService;
import com.fenoreste.service.ITablasService;
import com.fenoreste.service.TransactionsServiceSpring;
import com.github.cliftonlabs.json_simple.JsonObject;

@RestController
@CrossOrigin(origins = "")
@RequestMapping({ "/Transaction" })
public class TransactionController {
	
	@Autowired
	TransactionsServiceSpring transactionService;
	
	@Autowired
	ITablasService tbService;
	
	@Autowired
	IOtrosService otrosService;
	
	
	@PostMapping(value="/Insert",consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity insertTransaction(@RequestBody String cadena) {		
		if(!otrosService.servicios_activos()) {
			JsonObject json_horario=new JsonObject();
			json_horario.put("Error", "VERIFIQUE SU HORARIO DE ACTIVIDAD FECHA,HORA O CONTACTE A SU PROVEEEDOR");
			return ResponseEntity.status(400).body(json_horario);
		}
		    BackendOperationResultDTO backendOperationResult = new BackendOperationResultDTO();
	        backendOperationResult.setBackendCode("2");
	        backendOperationResult.setBackendMessage("Error en transaccion");
	        backendOperationResult.setBackendReference(null);
	        backendOperationResult.setIntegrationProperties("{}");
	        backendOperationResult.setIsError(true);
	        backendOperationResult.setTransactionIdenty("0");

	        JsonObject response_json_principal = new JsonObject();
	       
	        


	        /*================================================================
	                Obtenemos el request y lo pasamos a DTO
	        =================================================================*/
	        TransactionToOwnAccountsDTO dto = new TransactionToOwnAccountsDTO();
	        JsonObject response_json_secundario = new JsonObject();
	        JsonObject response_json_3 = new JsonObject();

	        try {

	        	JSONObject jsonRecibido = new JSONObject(cadena);
	            JSONObject insertTransaction = jsonRecibido.getJSONObject("inserTransactionInput");
	            JSONObject destinationDocumentId = insertTransaction.getJSONObject("destinationDocumentId");

	            DocumentIdTransaccionesDTO dto1 = new DocumentIdTransaccionesDTO();
	            dto1.setDocumentNumber(destinationDocumentId.getString("documentNumber"));
	            dto1.setDocumentType(destinationDocumentId.getString("documentType"));

	            DocumentIdTransaccionesDTO dto2 = new DocumentIdTransaccionesDTO();
	            dto1.setDocumentNumber(destinationDocumentId.getString("documentNumber"));
	            dto1.setDocumentType(destinationDocumentId.getString("documentType"));
	          
	            DocumentIdTransaccionesDTO dto3 = new DocumentIdTransaccionesDTO();
	            dto1.setDocumentNumber(destinationDocumentId.getString("documentNumber"));
	            dto1.setDocumentType(destinationDocumentId.getString("documentType"));

	            dto.setSubTransactionTypeId(Integer.parseInt(insertTransaction.getString("subTransactionTypeId")));
	            dto.setCurrencyId(insertTransaction.getString("currencyId"));
	            dto.setValueDate(insertTransaction.getString("valueDate"));
	            dto.setTransactionTypeId(insertTransaction.getInt("transactionTypeId"));
	            dto.setTransactionStatusId(insertTransaction.getInt("transactionStatusId"));
	            dto.setClientBankIdentifier(insertTransaction.getString("clientBankIdentifier"));
	            dto.setDebitProductBankIdentifier(insertTransaction.getString("debitProductBankIdentifier"));
	            dto.setDebitProductTypeId(insertTransaction.getInt("debitProductTypeId"));
	            dto.setDebitCurrencyId(insertTransaction.getString("debitCurrencyId"));
	            dto.setCreditProductBankIdentifier(insertTransaction.getString("creditProductBankIdentifier"));
	            dto.setCreditProductTypeId(insertTransaction.getInt("creditProductTypeId"));
	            dto.setCreditCurrencyId(insertTransaction.getString("creditCurrencyId"));
	            dto.setAmount(insertTransaction.getDouble("amount"));
	            dto.setNotifyTo(insertTransaction.getString("notifyTo"));
	            dto.setNotificationChannelId(insertTransaction.getInt("notificationChannelId"));
	            dto.setTransactionId(insertTransaction.getInt("transactionId"));
	            dto.setDestinationDocumentId(dto1);
	            dto.setDestinationName(insertTransaction.getString("destinationName"));
	            dto.setDestinationBank(insertTransaction.getString("destinationBank"));
	            dto.setDescription(insertTransaction.getString("description"));
	            dto.setBankRoutingNumber(insertTransaction.getString("bankRoutingNumber"));
	            dto.setSourceName(insertTransaction.getString("sourceName"));
	            dto.setSourceBank(insertTransaction.getString("sourceBank"));
	            dto.setSourceDocumentId(dto2);
	            dto.setRegulationAmountExceeded(insertTransaction.getBoolean("regulationAmountExceeded"));
	            dto.setSourceFunds(insertTransaction.getString("sourceFunds"));
	            dto.setDestinationFunds(insertTransaction.getString("destinationFunds"));
	            dto.setUserDocumentId(dto3);
	            dto.setTransactionCost(insertTransaction.getDouble("transactionCost"));
	            dto.setTransactionCostCurrencyId(insertTransaction.getString("transactionCostCurrencyId"));
	            dto.setExchangeRate(insertTransaction.getDouble("exchangeRate"));
	            dto.setCountryIntermediaryInstitution(insertTransaction.getString("countryIntermediaryInstitution"));
	            dto.setRouteNumberIntermediaryInstitution("{}");
	            dto.setIntegrationParameters("{}");
	        } catch (Exception e) {
	            backendOperationResult.setBackendCode("2");
	            backendOperationResult.setBackendMessage(e.getMessage());
	            response_json_3.put("integrationProperties", backendOperationResult.getIntegrationProperties());
	            response_json_3.put("backendCode", backendOperationResult.getBackendCode());
	            response_json_3.put("backendMessage", backendOperationResult.getBackendMessage());
	            response_json_3.put("backendReference", null);
	            response_json_3.put("isError", backendOperationResult.isIsError());
	            response_json_3.put("transactionType", backendOperationResult.getTransactionIdenty());

	            response_json_secundario.put("backendOperationResult", response_json_3);
	            response_json_principal.put("InsertTransactionResult", response_json_secundario);
	            return ResponseEntity.status(400).body(response_json_principal);
	        }

	        /*======================================================================
	                Si el request que nos llego es el correcto procedemos
	          ======================================================================*/	        
	        try {
	            System.out.println("Accediendo a trasnferencias con subTransactionType=" + dto.getSubTransactionTypeId() + ",TransactionId:" + dto.getTransactionTypeId());
	           //Si subtransactionType es 1 y transactionType es 1: El tipo de transaccion es es entre mis cuentas
	            if (dto.getSubTransactionTypeId() == 1 && dto.getTransactionTypeId() == 1) {
	                backendOperationResult = transactionService.transferencias(dto, 1, null);
	            }
	            //Si subtransactionType es 2 y transactionType es 1: El tipo de transaccion es a terceros
	            if (dto.getSubTransactionTypeId() == 2 && dto.getTransactionTypeId() == 1) {
	                backendOperationResult = transactionService.transferencias(dto, 2, null);
	            }
	            //Si subtransactionType es 9 y transactionType es 6: El tipo de transaccion es es un pago a prestamos 
	            if (dto.getSubTransactionTypeId() == 9 && dto.getTransactionTypeId() == 6) {
	                backendOperationResult = transactionService.transferencias(dto, 3, null);
	            }
	            //Si es un pago a prestamo tercero
	            if (dto.getSubTransactionTypeId() == 10 && dto.getTransactionTypeId() == 6) {
	            	System.out.println("Si entro en pago a prestamo de tercero");
	                backendOperationResult = transactionService.transferencias(dto, 4, null);
	            }
	            //Si es una trasnferencia SPEI
	            if (dto.getSubTransactionTypeId() == 3 && dto.getTransactionTypeId() == 1) {
	                //Consumimos mis servicios de SPEI que tengo en otro proyecto(CSN0)
	                RequestDataOrdenPagoDTO ordenReque = new RequestDataOrdenPagoDTO();
	                ordenReque.setClienteClabe(dto.getDebitProductBankIdentifier());//Opa origen como cuenta clabe en el metodo spei se busca la clave
	                ordenReque.setConceptoPago(dto.getDescription());
	                ordenReque.setCuentaBeneficiario(dto.getCreditProductBankIdentifier());//La clabe del beneficiario
	                ordenReque.setInstitucionContraparte(dto.getDestinationBank());
	                ordenReque.setMonto(dto.getAmount());
	                ordenReque.setNombreBeneficiario(dto.getDestinationName());
	                ordenReque.setRfcCurpBeneficiario(dto.getDestinationDocumentId().getDocumentNumber());
	                ordenReque.setOrdernante(dto.getClientBankIdentifier());

	                backendOperationResult = transactionService.transferencias(dto, 5, ordenReque);
	            }

	            response_json_3.put("integrationProperties",null);
	            response_json_3.put("backendCode", backendOperationResult.getBackendCode());
	            response_json_3.put("backendMessage", backendOperationResult.getBackendMessage());
	            response_json_3.put("backendReference", null);
	            response_json_3.put("isError", backendOperationResult.isIsError());
	            response_json_3.put("transactionType", backendOperationResult.getTransactionIdenty());

	            response_json_secundario.put("backendOperationResult", response_json_3);
	            response_json_principal.put("InsertTransactionResult", response_json_secundario);
	           

	        } catch (Exception e) {
	            backendOperationResult.setBackendMessage(e.getMessage());
	            response_json_3.put("integrationProperties", null);
	            response_json_3.put("backendCode", backendOperationResult.getBackendCode());
	            response_json_3.put("backendMessage", backendOperationResult.getBackendMessage());
	            response_json_3.put("backendReference", null);
	            response_json_3.put("isError", backendOperationResult.isIsError());
	            response_json_3.put("transactionType", backendOperationResult.getTransactionIdenty());

	            response_json_secundario.put("backendOperationResult", response_json_3);
	            response_json_principal.put("InsertTransactionResult", response_json_secundario);
	            
	            return ResponseEntity.status(500).body(response_json_principal);
	        }
	        return ResponseEntity.status(200).body(response_json_principal);
	    }
	
	@PostMapping(value="/Voucher",consumes = { MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
	 public VaucherDTO voucher(@RequestBody String cadena) {
	        System.out.println("Entrando:"+cadena);
	        String idTransaccion = "";	       
	       JsonObject jsonMessage = new JsonObject();
	       VaucherDTO voucher = null;
	        try {
	        	JSONObject request = new JSONObject(cadena);
	        	idTransaccion = request.getString("transactionVoucherIdentifier");
	            voucher = transactionService.vaucher(idTransaccion);
	        } catch (Exception e) {
	            //jsonMessage.put("Error", e.getMessage());
	            //return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonMessage).build();
	        }
	        return voucher;
	    }


	
}
