package com.fenoreste.controller;

import java.util.ArrayList;
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

import com.fenoreste.dto.AccountDetailsDTO;
import com.fenoreste.dto.AccountLastMovementDTO;
import com.fenoreste.service.AccountServiceSpring;
import com.github.cliftonlabs.json_simple.JsonObject;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping({ "/Account" })
public class AccountController {

	@Autowired
	AccountServiceSpring accountService;

	@PostMapping(value = "/Details", consumes = { MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity details(@RequestBody String cadena) {
		String accountId = "";
		JsonObject Json_De_Error = new JsonObject();
		AccountDetailsDTO cuenta_dto=null;
		try {
		    JSONObject jsonRecibido = new JSONObject(cadena);
			accountId = jsonRecibido.getString("productBankIdentifier");
			cuenta_dto = accountService.getAccountDetails(accountId);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return ResponseEntity.ok(cuenta_dto);
	}

	@PostMapping(value = "/Last5Movements", consumes = { MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity last5Movements(@RequestBody String cadena) {		
		JsonObject Json_De_Error = new JsonObject();
		JsonObject cuentasJson = null;
        try {
        	JSONObject jsonRecibido = new JSONObject(cadena);
    		String accountId = jsonRecibido.getString("productBankIdentifier");

    		List<AccountLastMovementDTO> last5Movements = new ArrayList<>();

    		last5Movements = accountService.getAccountLast5Movements(accountId);
    		cuentasJson = new JsonObject();
    		cuentasJson.put("Last5Movements", last5Movements);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return ResponseEntity.ok(cuentasJson);
	}
	
	
	@PostMapping(value = "/Movements", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity Movements(@RequestBody String cadena) {
        System.out.println("Cadena en movements:"+cadena);
		String ProductBankIdentifier = "";
        String DateFromFilter = null;
        String DateToFilter = null;
        int PageSize = 0;
        int PageStartIndex = 0;        
        JsonObject Error=new JsonObject();
        String orderBy="";
        JsonObject response = new JsonObject();
        try {
        JSONObject jsonRecibido = new JSONObject(cadena);
        ProductBankIdentifier = jsonRecibido.getString("productBankIdentifier");
        DateFromFilter = jsonRecibido.getString("dateFromFilter");
        DateToFilter = jsonRecibido.getString("dateToFilter");
        JSONObject json = jsonRecibido.getJSONObject("paging");
        PageSize = json.getInt("pageSize");
        PageStartIndex = json.getInt("pageStartIndex");
        orderBy=json.getString("orderByField");
        
       
        List<AccountLastMovementDTO> MiListaDTO=accountService.getAccountMovements(ProductBankIdentifier, DateFromFilter, DateToFilter, PageSize, PageStartIndex, orderBy);
        response.put("MovementsCount",MiListaDTO.size());
        response.put("Movements", MiListaDTO);
        }catch (Exception e) {
			// TODO: handle exception
		}
        return ResponseEntity.ok(response);
	}
	
	
}