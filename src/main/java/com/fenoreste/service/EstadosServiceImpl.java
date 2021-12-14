package com.fenoreste.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fenoreste.dao.EstadosRepository;
import com.fenoreste.entity.Estados;
import com.fenoreste.entity.Municipios;

@Service
public class EstadosServiceImpl implements IEstadosService{

	@Autowired
	EstadosRepository estadosDao;
	
	@Override
	public Estados findEstadoById(Integer id) {
		// TODO Auto-generated method stub
		return estadosDao.findEstadoById(id);
	}

}
