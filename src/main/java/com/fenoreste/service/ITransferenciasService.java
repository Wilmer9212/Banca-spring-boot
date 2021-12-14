package com.fenoreste.service;

import org.springframework.stereotype.Service;

import com.fenoreste.entity.Transferencias;

@Service
public interface ITransferenciasService {
   
	public void guardaTransferencia(Transferencias transferencia);
	
}
