package com.fenoreste.service;

import org.springframework.stereotype.Service;

import com.fenoreste.entity.TiposCuentaBankingly;

@Service
public interface ITiposCuentaBankinglyService {
	
	public TiposCuentaBankingly findTipoCuentaById(Integer idproducto);

}
