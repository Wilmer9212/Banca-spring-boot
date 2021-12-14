package com.fenoreste.service;


import com.fenoreste.entity.Tablas;

public interface ITablasService {

	public Tablas findTabla(String idtabla, String idelemento);
	
	public Tablas findIdtablaAndIdelemento(String idtabla,String idelemento);

}
