package com.fenoreste.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fenoreste.service.CustomerServiceSpring;
import com.fenoreste.service.IOtrosService;
import com.github.cliftonlabs.json_simple.JsonObject;


@RestController
@RequestMapping({"/Clients" })
public class CustomerController {
	
    @Autowired
	private CustomerServiceSpring bankinglyService;
	
	@Autowired
	IOtrosService otrosService;

	@PostMapping(value = "/ByDocuments", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity personaFind(@RequestBody String cadena) {	
		
		if(!otrosService.servicios_activos()) {
			JsonObject json_horario=new JsonObject();
			json_horario.put("Error", "VERIFIQUE SU HORARIO DE ACTIVIDAD FECHA,HORA O CONTACTE A SU PROVEEEDOR");
			return ResponseEntity.status(400).body(json_horario);
		}
		
		String DocumentId = "";
		String Name = "";
		String LastName = "";
		String Mail = "";
		String Phone = "";
		String CellPhone = "";
		String UserName = "";
		
		JsonObject response = new JsonObject();
		
		//Antes de todo verifico la hora de actividad		
		try {

			JSONObject jsonRequest_ = new JSONObject(cadena);
			DocumentId = jsonRequest_.getString("documentId");
			Name = jsonRequest_.getString("name");
			LastName = jsonRequest_.getString("lastName");
			Mail = jsonRequest_.getString("mail");
			Phone = jsonRequest_.getString("phone");
			CellPhone = jsonRequest_.getString("cellPhone");
			UserName = jsonRequest_.getString("userName");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		response.put("customers",bankinglyService.getClientsByDocuments(Name.toUpperCase(), LastName.toUpperCase(), DocumentId.toUpperCase(), Mail.toUpperCase(), CellPhone, Phone,UserName));
		
		return ResponseEntity.ok(response);
	}

}
