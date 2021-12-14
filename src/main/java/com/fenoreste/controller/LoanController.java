package com.fenoreste.controller;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fenoreste.dto.LoanDTO;
import com.fenoreste.dto.LoanFee;
import com.fenoreste.dto.LoanPayment;
import com.fenoreste.dto.LoanRate;
import com.fenoreste.dto.opaDTO;
import com.fenoreste.service.IAmortizacionesService;
import com.fenoreste.service.IOtrosService;
import com.fenoreste.service.LoanServiceSpring;
import com.fenoreste.util.HerramientasUtil;
import com.github.cliftonlabs.json_simple.JsonObject;

@RestController
@CrossOrigin(origins = "")
@RequestMapping({ "/Loan" })
public class LoanController {

	@Autowired
	LoanServiceSpring loanService;
	@Autowired 
	IAmortizacionesService amortizacionesService;
	
	@Autowired
	IOtrosService otrosService;
	
	@Autowired
	HerramientasUtil util;
	
	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity getLoan(@RequestBody String cadena) {
		if(!otrosService.servicios_activos()) {
			JsonObject json_horario=new JsonObject();
			json_horario.put("Error", "VERIFIQUE SU HORARIO DE ACTIVIDAD FECHA,HORA O CONTACTE A SU PROVEEEDOR");
			return ResponseEntity.status(400).body(json_horario);
		}
		
		System.out.println("Cadena get loan:" + cadena);
		String productBankIdentifier = "";
		JsonObject Error = new JsonObject();

		try {
			JSONObject jsonRecibido = new JSONObject(cadena);
			productBankIdentifier = jsonRecibido.getString("productBankIdentifier");
		} catch (Exception e) {
			Error.put("Error", "Error en parametros JSON:" + e.getMessage());
			return ResponseEntity.status(500).body(Error);
		}

		int count = 0;

		LoanDTO loan = loanService.Loan(productBankIdentifier);
		JsonObject j = new JsonObject();
		j.put("Loan", loan);
		return ResponseEntity.status(200).body(j);
	}

	@PostMapping(value = "/Fee", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity getLoanFee(@RequestBody String cadena) {
		if(!otrosService.servicios_activos()) {
			JsonObject json_horario=new JsonObject();
			json_horario.put("Error", "VERIFIQUE SU HORARIO DE ACTIVIDAD FECHA,HORA O CONTACTE A SU PROVEEEDOR");
			return ResponseEntity.status(400).body(json_horario);
		}
		String productBankIdentifier = "";
		JsonObject Error = new JsonObject();
		int feeNumber = 0;

		try {
			JSONObject jsonRecibido = new JSONObject(cadena);
			productBankIdentifier = jsonRecibido.getString("productBankIdentifier");
		LoanFee loan = loanService.LoanFee(productBankIdentifier, feeNumber);
		JsonObject j = new JsonObject();
		j.put("Fee", loan);
		return ResponseEntity.status(200).body(j);
		} catch(Exception e) {
		   System.out.println("Error en metodo loan Fee:"+e.getMessage());
		   Error.put("Error",e.getMessage());
		   return ResponseEntity.status(500).body(e.getMessage());
	    }
		
	}

	@PostMapping(value = "/Fees", consumes = { MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,					MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity getLoanFees(@RequestBody String cadena) {	
		if(!otrosService.servicios_activos()) {
			JsonObject json_horario=new JsonObject();
			json_horario.put("Error", "VERIFIQUE SU HORARIO DE ACTIVIDAD FECHA,HORA O CONTACTE A SU PROVEEEDOR");
			return ResponseEntity.status(400).body(json_horario);
		}
		String productBankIdentifier = "";
		int feesStatus = 0, pageSize = 0, pageStartIndex = 0;
		JsonObject j = new JsonObject();
		try {
			    String order = "";
			    JSONObject jsonRecibido = new JSONObject(cadena);
			    System.out.println("Cadena Fees result data: "+jsonRecibido);
				productBankIdentifier = jsonRecibido.getString("productBankIdentifier");
				feesStatus = jsonRecibido.getInt("feesStatus");
				JSONObject paging = jsonRecibido.getJSONObject("paging");
				if(!paging.toString().contains("null")) {
					pageSize = paging.getInt("pageSize");
					pageStartIndex = paging.getInt("pageStartIndex");
					if(String.valueOf(pageStartIndex).length() == 2) {
						pageStartIndex=Integer.parseInt(String.valueOf(pageStartIndex).substring(0,1));
					}else if(String.valueOf(pageStartIndex).length() == 3) {
						pageStartIndex=Integer.parseInt(String.valueOf(pageStartIndex).substring(0,2));
					}else if(String.valueOf(pageStartIndex).length() == 4) {
						pageStartIndex=Integer.parseInt(String.valueOf(pageStartIndex).substring(0,3));
					}else if(String.valueOf(pageStartIndex).length() == 5) {
						pageStartIndex=Integer.parseInt(String.valueOf(pageStartIndex).substring(0,4));
					}else if(String.valueOf(pageStartIndex).length() == 6) {
						pageStartIndex=Integer.parseInt(String.valueOf(pageStartIndex).substring(0,5));
					}
					order = paging.getString("orderByField");
					
				}				
				opaDTO opa=util.opa(productBankIdentifier);					
				List<LoanFee> loan = loanService.LoanFees(productBankIdentifier, feesStatus, pageSize, pageStartIndex, order);//    dao.LoanFees(productBankIdentifier, feesStatus, pageSize, pageStartIndex, order);
							
				int tot=0;
				if(feesStatus == 1) {
				tot= amortizacionesService.findCountAmortizacionesPagadas(opa.getIdorigenp(),opa.getIdproducto(),opa.getIdauxiliar());
				}else if(feesStatus == 2) {
			    tot = amortizacionesService.findCountAmortizacionesActivas(opa.getIdorigenp(),opa.getIdproducto(),opa.getIdauxiliar());		
				}else {
				tot = amortizacionesService.findCountAmortizaciones(opa.getIdorigenp(),opa.getIdproducto(),opa.getIdauxiliar());	
				}
				
			    j.put("Fees", loan);
				j.put("LoanFeesCount",tot);
	
			
		} catch (Exception e) {
			System.out.println("Error en metodo loan Fees ws:" + e.getMessage());
		}
		return ResponseEntity.status(200).body(j);
	}

	@PostMapping(value = "/Rates", consumes = { MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity getLoanRates(@RequestBody String cadena) {	
		if(!otrosService.servicios_activos()) {
			JsonObject json_horario=new JsonObject();
			json_horario.put("Error", "VERIFIQUE SU HORARIO DE ACTIVIDAD FECHA,HORA O CONTACTE A SU PROVEEEDOR");
			return ResponseEntity.status(400).body(json_horario);
	  	 }
		String productBankIdentifier = "";
		JsonObject Error = new JsonObject();
		int pageSize = 0, pageStartIndex = 0;		
		JsonObject j = new JsonObject();
		    try {
		    	JSONObject jsonRecibido = new JSONObject(cadena);
				productBankIdentifier = jsonRecibido.getString("productBankIdentifier");
				JSONObject paging = jsonRecibido.getJSONObject("paging");
				if(!paging.toString().contains("null")) {
					pageSize = paging.getInt("pageSize");
					pageStartIndex = paging.getInt("pageStartIndex");
				}
    			List<LoanRate> loanRate = loanService.LoanRates(productBankIdentifier, pageSize, pageStartIndex);				
				j.put("Rates", loanRate);
				j.put("LoanRatesCount", loanRate.size());
				
				return ResponseEntity.status(200).body(j);
			} catch (Exception e) {
				System.out.println("Error en metodo loan rates:"+e.getMessage());
				Error.put("Error", e.getMessage());
				return ResponseEntity.status(500).body(Error);
			}
	}
	
	@PostMapping(value = "/ssPaymentssssss", consumes = { MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity getLoanPayments(@RequestBody String cadena) {	
		System.out.println("Entrando a loanPayments con cadena:"+cadena);
		if(!otrosService.servicios_activos()) {
			JsonObject json_horario=new JsonObject();
			json_horario.put("Error", "VERIFIQUE SU HORARIO DE ACTIVIDAD FECHA,HORA O CONTACTE A SU PROVEEEDOR");
			return ResponseEntity.status(400).body(json_horario);
		}
		
		String productBankIdentifier = "";
		JsonObject Error = new JsonObject();
		int pageSize = 0, pageStartIndex = 0;
		try {
			JSONObject jsonRecibido = new JSONObject(cadena);
			productBankIdentifier = jsonRecibido.getString("productBankIdentifier");
			JSONObject paging = jsonRecibido.getJSONObject("paging");
			if(!paging.toString().contains("null")) {
				pageSize = paging.getInt("pageSize");
				pageStartIndex = paging.getInt("pageStartIndex");
			}		
		} catch (Exception e) {
			Error.put("Error", "Error en parametros JSON:" + e.getMessage());
			return ResponseEntity.status(400).body(Error);
		}

		int count = 0;
			List<LoanPayment> ListPayment = loanService.loanPayments(productBankIdentifier, pageStartIndex,pageSize);
			opaDTO opa = util.opa(productBankIdentifier);
			JsonObject j = new JsonObject();
			j.put("Payments", ListPayment);		
			j.put("LoanPaymentsCount",amortizacionesService.findCountAmortizaciones(opa.getIdorigenp(),opa.getIdproducto(),opa.getIdauxiliar()));
			return ResponseEntity.status(200).body(j);
		
	}
}
