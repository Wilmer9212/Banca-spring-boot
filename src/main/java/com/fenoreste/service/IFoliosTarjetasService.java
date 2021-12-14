package com.fenoreste.service;

import org.springframework.stereotype.Service;

import com.fenoreste.entity.FoliosTarjeta;

@Service
public interface IFoliosTarjetasService {
 
	public FoliosTarjeta findFolioByOpa(int idorigenp,int idproducto,int idauxiliar);
	
	

}
