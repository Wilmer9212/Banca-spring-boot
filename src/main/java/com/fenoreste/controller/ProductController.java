package com.fenoreste.controller;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.nio.file.Paths;

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

import com.fenoreste.dto.ProductBankStatementDTO;
import com.fenoreste.dto.ProductConsolidatedPositionDTO;
import com.fenoreste.dto.ProductosDTO;
import com.fenoreste.service.IOtrosService;
import com.fenoreste.service.ProductsServiceSpring;
import com.github.cliftonlabs.json_simple.JsonObject;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping({ "/Products" })
public class ProductController {

	@Autowired
	ProductsServiceSpring productosServiceSpring;
	
	@Autowired
	IOtrosService otrosService;
	
	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity getProducts(@RequestBody String cadena) {
	
		if(!otrosService.servicios_activos()) {
			JsonObject json_horario=new JsonObject();
			json_horario.put("Error", "VERIFIQUE SU HORARIO DE ACTIVIDAD FECHA,HORA O CONTACTE A SU PROVEEEDOR");
			return ResponseEntity.status(400).body(json_horario);
		}
		
		
		String ClientBankIdentifiers = "";
		Integer ProductTypes = null;
		JsonObject jsonError = new JsonObject();

		// -------------------------Obtiene el request json---------------------*/
		try {
			JSONObject Object = new JSONObject(cadena);
			JSONArray jsonCB = Object.getJSONArray("clientBankIdentifiers");
			JSONArray jsonPB = Object.getJSONArray("productTypes");

			for (int i = 0; i < jsonCB.length(); i++) {
				JSONObject jCB = (JSONObject) jsonCB.get(i);
				ClientBankIdentifiers = jCB.getString("value");
			}
			for (int x = 0; x < jsonPB.length(); x++) {
				JSONObject jPB = (JSONObject) jsonPB.get(x);
				ProductTypes = jPB.getInt("value");
			}
		} catch (Exception e) {
			jsonError.put("Error", "Request Failed");
			return ResponseEntity.status(400).body(jsonError);
		}

		try {
			List<ProductosDTO> listaDTO = productosServiceSpring.listaProductos(ClientBankIdentifiers,ProductTypes);
			
			if (listaDTO != null) {
				JsonObject json_productos = new JsonObject();
				json_productos.put("Products", listaDTO);
				return ResponseEntity.status(200).body(json_productos);
			} else {
				jsonError.put("Error", "DATOS NO ENCONTRADOS");
				return ResponseEntity.status(500).body(jsonError);
			}
		} catch (Exception e) {
			return ResponseEntity.status(500).body(e.getMessage());
		}
	}

	@PostMapping(value = "/ConsolidatedPosition", consumes = { MediaType.APPLICATION_JSON_VALUE}, produces = { MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity getProductsConsolidatedPosition(@RequestBody String cadena) {
		if(!otrosService.servicios_activos()) {
			JsonObject json_horario=new JsonObject();
			json_horario.put("Error", "VERIFIQUE SU HORARIO DE ACTIVIDAD FECHA,HORA O CONTACTE A SU PROVEEEDOR");
			return ResponseEntity.status(400).body(json_horario);
		}
		String ClientBankIdentifiers = "", ProductBankIdentifiers = "";

		JsonObject jsonError = new JsonObject();
		List<String> productsBank = new ArrayList<String>();

		// Genero una lista de opas a consultar datos
		try {
			JSONObject Object = new JSONObject(cadena);
			JSONArray jsonCB = Object.getJSONArray("clientBankIdentifiers");
			JSONArray jsonPB = Object.getJSONArray("productBankIdentifiers");
			
			//Corro la lista de clientes
			for (int i = 0; i < jsonCB.length(); i++) {
				JSONObject jCB = (JSONObject) jsonCB.get(i);
				ClientBankIdentifiers = jCB.getString("value");

				System.out.println("ClientBankIdentifiers:" + ClientBankIdentifiers);
			}
			//corro la lista de productos
			for (int x = 0; x < jsonPB.length(); x++) {
				JSONObject jPB = jsonPB.getJSONObject(x);
				ProductBankIdentifiers = jPB.getString("value");
				productsBank.add(ProductBankIdentifiers);
			}
		} catch (Exception e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}

		try {
            List<ProductConsolidatedPositionDTO> ListProductos = productosServiceSpring.consolidatedPosition(ClientBankIdentifiers, productsBank);
            if (ListProductos != null) {
                JsonObject json_productos = new JsonObject();
                json_productos.put("ConsolidatedPosition", ListProductos);
                return ResponseEntity.status(200).body(json_productos);

            } else {
                jsonError.put("Error", "DATOS NO ENCONTRADOS");
                return ResponseEntity.status(204).body(jsonError);
            }
        } catch (Exception e) {
            System.out.println("Error aqui:" + e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        } 
	}
	
	@PostMapping(value = "/BankStatements", consumes = { MediaType.APPLICATION_JSON_VALUE}, produces = { MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity getBankStatements(@RequestBody String cadena) {
		if(!otrosService.servicios_activos()) {
			JsonObject json_horario=new JsonObject();
			json_horario.put("Error", "VERIFIQUE SU HORARIO DE ACTIVIDAD FECHA,HORA O CONTACTE A SU PROVEEEDOR");
			return ResponseEntity.status(400).body(json_horario);
		}
		JsonObject json = new JsonObject();
        
		try {
			JSONObject request_ = new JSONObject(cadena);
	        String clientBankIdentifier_ = "";
	        String productBankIdentifier_ = "";
	        int productType_ = 0;
	        
	        clientBankIdentifier_ = request_.getString("clientBankIdentifier");
	        productBankIdentifier_ = request_.getString("productBankIdentifier");
	        productType_ = request_.getInt("productType");
	        List<ProductBankStatementDTO> listaECuentas = productosServiceSpring.statements(clientBankIdentifier_, productBankIdentifier_, productType_);
	        if(listaECuentas != null) {
	        	json.put("bankStatements", listaECuentas);
	        }else {
	        	json.put("bankStatements","error al buscar estados de cuenta");
	        }   	
		} catch (Exception e) {
			// TODO: handle exception
		}
		     
       return ResponseEntity.ok(json);        
	}
	
	
	@PostMapping(value = "/BankStatementsFile", consumes = { MediaType.APPLICATION_JSON_VALUE}, produces = { MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity StatementsFile(@RequestBody String cadena) { 
		if(!otrosService.servicios_activos()) {
			JsonObject json_horario=new JsonObject();
			json_horario.put("Error", "VERIFIQUE SU HORARIO DE ACTIVIDAD FECHA,HORA O CONTACTE A SU PROVEEEDOR");
			return ResponseEntity.status(400).body(json_horario);
		}
        JsonObject jsonMessage=new JsonObject();  
        
        try {
        	JSONObject RequestData = new JSONObject(cadena);
            String fileId = "";      
            fileId = RequestData.getString("productBankStatementId");
            
            String filePath = ruta() + fileId + ".pdf";
            File file = new File(filePath);
            if (file.exists()) {
                byte[] input_file = Files.readAllBytes(Paths.get(filePath));
                byte[] encodedBytesFile = Base64.getEncoder().encode(input_file);
                String bytesFileId = new String(encodedBytesFile);
                jsonMessage.put("productBankStatementFile",bytesFileId);
                jsonMessage.put("productBankStatementFileName",fileId+".pdf");
            }else{
                jsonMessage.put("Error","EL ARCHIVO QUE INTENTA DESCARGAR NO EXISTE");
            }
            
        } catch (Exception e) {
            jsonMessage.put("Error",e.getMessage());
            return ResponseEntity.status(500).body(jsonMessage);
        }
        return ResponseEntity.status(200).body(jsonMessage);
    }	
	
	//Parao obtener la ruta del servidor
    public static String ruta() {
        String home = System.getProperty("user.home");
        String separador = System.getProperty("file.separator");
        String actualRuta = home + separador + "Banca" + separador;
        return actualRuta;
    }

}
