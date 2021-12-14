package com.fenoreste.service;

import org.springframework.stereotype.Service;

import com.fenoreste.entity.Origenes;

@Service
public interface IOrigenesService {

	public Origenes findMatrizOrigen();
	
	public Origenes findOrigenById(Integer idorigen);
	
	public String fechaTrabajo();
}
