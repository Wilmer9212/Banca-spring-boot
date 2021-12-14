package com.fenoreste.consumo;

import org.springframework.stereotype.Service;

@Service
public interface IConsumosHTTPService {
	
	public String getBalanceQuery(String idtarjeta);
	
	public boolean DoWithdrawal(String idtarjeta,Double monto);

	public boolean  LoadBalance(String idtarjeta,Double monto);
}
