package com.fenoreste.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fenoreste.dao.CatalogoRepository;
import com.fenoreste.entity.CatalogoEstatus;

@Service
public class CatalogoEstatusServiceImpl implements ICatalogoService {
	
	@Autowired
	CatalogoRepository catalogoDao;
	
	@Override
	public CatalogoEstatus findCatalogoById(Integer idEstatus) {
		return catalogoDao.findCatalogoByEstatus(idEstatus);
	}

}
