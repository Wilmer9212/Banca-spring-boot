package com.fenoreste.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fenoreste.dao.GeneralRepository;

@Service
public class GeneralServiceImpl implements IGeneralService {

	@Autowired
	GeneralRepository generalDao;
	
	@Transactional(readOnly = true)
	public String activo(){
		return generalDao.activo();
	}
}
