package com.fenoreste.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fenoreste.dao.OtrosRepository;
import com.fenoreste.dao.UsuariosBancaMovilRepository;
import com.fenoreste.entity.FoliosTarjeta;

@Service
public class OtrosServiceImpl implements IOtrosService {

	@Autowired
	OtrosRepository otrosDao;
	
	@Override
	public Integer count_sopar(Integer idorigen, Integer idgrupo, Integer idsocio, String tipo) {
		return otrosDao.count_sopar(idorigen, idgrupo, idsocio, tipo) ;
	}

	@Override
	public String sesion() {
		// TODO Auto-generated method stub
		return otrosDao.sesion();
	}

	@Override
	public boolean servicios_activos() {
		// TODO Auto-generated method stub
		return otrosDao.servicios_activos();
	}

	
	
	

}
