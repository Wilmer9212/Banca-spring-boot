package com.fenoreste.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fenoreste.dao.PaisesRepository;
import com.fenoreste.entity.Paises;

@Service
public class PaisesServiceImpl implements IPaisesService {

	@Autowired
	PaisesRepository paisesDao;
	
	@Override
	public Paises findPaisById(Integer id) {
		// TODO Auto-generated method stub
		return paisesDao.findPaisById(id);
	}

}
