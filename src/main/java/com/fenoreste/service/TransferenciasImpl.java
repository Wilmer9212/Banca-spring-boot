package com.fenoreste.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fenoreste.dao.TransferenciasRepository;
import com.fenoreste.entity.Transferencias;

@Service
public class TransferenciasImpl implements ITransferenciasService {
    
	@Autowired
	TransferenciasRepository transferenciasDao;
	
	@Override
	public void guardaTransferencia(Transferencias transferencia) {
	transferenciasDao.save(transferencia);	
	}
    
}
