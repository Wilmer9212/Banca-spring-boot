package com.fenoreste.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fenoreste.dao.TiposCuentaBankinglyRepository;
import com.fenoreste.entity.TiposCuentaBankingly;

@Service
public class TipoCuentaBankinglyServiceImpl  implements ITiposCuentaBankinglyService{
	 
	@Autowired
	TiposCuentaBankinglyRepository cuentaBankinglyDao;
	
	@Override
	public TiposCuentaBankingly findTipoCuentaById(Integer idproducto) {
	return cuentaBankinglyDao.findTipoCuentaById(idproducto);
	}

}
