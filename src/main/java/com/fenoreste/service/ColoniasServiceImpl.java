package com.fenoreste.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fenoreste.dao.ColoniasRepository;
import com.fenoreste.entity.Colonia;

@Service
public class ColoniasServiceImpl implements IColoniasService{

	@Autowired
	ColoniasRepository coloniasDao;

	@Override
	public Colonia findColoniaById(Integer id) {
		// TODO Auto-generated method stub
		return coloniasDao.findColoniaById(id);
	}



	
}
