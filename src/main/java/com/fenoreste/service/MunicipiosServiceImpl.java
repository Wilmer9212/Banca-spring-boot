package com.fenoreste.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fenoreste.dao.MunicipiosRepository;
import com.fenoreste.entity.Municipios;

@Service
public class MunicipiosServiceImpl  implements IMunicipiosService{

	@Autowired
	MunicipiosRepository municipiosDao;

	@Override
	public Municipios findMunicipioById(Integer id) {
		// TODO Auto-generated method stub
		return municipiosDao.findMunicipioById(id);
	}
	
	
}
