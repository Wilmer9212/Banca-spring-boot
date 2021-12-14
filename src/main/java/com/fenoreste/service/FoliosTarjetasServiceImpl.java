package com.fenoreste.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fenoreste.dao.FoliosTarjetasRepository;
import com.fenoreste.entity.FoliosTarjeta;

@Service
public class FoliosTarjetasServiceImpl implements IFoliosTarjetasService  {
	
	@Autowired
	FoliosTarjetasRepository foliosDao;

	@Override
	public FoliosTarjeta findFolioByOpa(int idorigenp, int idproducto,int idauxiliar) {
	return foliosDao.findFolioByOpa(idorigenp, idproducto, idauxiliar);
	}

}
