package com.fenoreste.consumo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ConsumosHTTPServiceImpl  implements IConsumosHTTPService{

	private static RestTemplate restTemplate = new RestTemplate();
	
	private static final String basePath="http://192.168.99.37:8080/WSDL_TDD_CSN";
	private static final String urlBalance=basePath+"/tarjetas/service/getBalanceQuery/";
	private static final String urlWithdrawal=basePath+"/tarjetas/service/doWithdrawal/";
	private static final String urlLoadBalance=basePath+"/tarjetas/service/loadBalance/";
	


	@Override
	public String getBalanceQuery(String idtarjeta) {
		HttpHeaders headers = new HttpHeaders(); 
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("user","ws_sanNico");
		params.put("pass","xZ4XTX3");
		params.put("host","");
		params.put("port", "8080");
		params.put("wsdl","");
		params.put("idtarjeta", "");
		
		HttpEntity<Map<String, String>> request = null;
		ResponseEntity<String> result = null;
		try {
			request = new HttpEntity<Map<String, String>>(params, headers);
			result = restTemplate.postForEntity(urlBalance, request, String.class);
			System.out.println(result.getBody());
			return result.getBody();
		} catch (Exception e) {
			System.out.println("mensaje:" + e.getMessage());
			return e.getMessage();
		}
	}


	@Override
	public boolean DoWithdrawal(String idtarjeta, Double monto) {
		HttpHeaders headers = new HttpHeaders(); 
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("user","ws_sanNico");
		params.put("pass","xZ4XTX3");
		params.put("host","");
		params.put("port", "8080");
		params.put("wsdl","");
		params.put("idtarjeta", idtarjeta);
		
		HttpEntity<Map<String, String>> request = null;
		ResponseEntity<String> result = null;
		try {
			request = new HttpEntity<Map<String, String>>(params, headers);
			result = restTemplate.postForEntity(urlBalance, request, String.class);
			System.out.println(result.getBody());
			return true;//result.getBody();
		} catch (Exception e) {
			System.out.println("mensaje:" + e.getMessage());
			return false;//e.getMessage();
		}
	}


	@Override
	public boolean LoadBalance(String idtarjeta, Double monto) {
		HttpHeaders headers = new HttpHeaders(); 
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("user","ws_sanNico");
		params.put("pass","xZ4XTX3");
		params.put("host","");
		params.put("port", "8080");
		params.put("wsdl","");
		params.put("idtarjeta", "");
		
		HttpEntity<Map<String, String>> request = null;
		ResponseEntity<String> result = null;
		try {
			request = new HttpEntity<Map<String, String>>(params, headers);
			result = restTemplate.postForEntity(urlBalance, request, String.class);
			System.out.println(result.getBody());
			return false;//result.getBody();
		} catch (Exception e) {
			System.out.println("mensaje:" + e.getMessage());
			return false;//e.getMessage();
		}
	}
	
	
}
