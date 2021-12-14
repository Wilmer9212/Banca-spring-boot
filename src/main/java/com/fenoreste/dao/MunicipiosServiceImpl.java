package com.fenoreste.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fenoreste.entity.Municipios;
import com.fenoreste.service.IMunicipiosService;

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
