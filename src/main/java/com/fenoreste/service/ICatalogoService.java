package com.fenoreste.service;

import org.springframework.stereotype.Service;

import com.fenoreste.entity.CatalogoEstatus;

@Service
public interface ICatalogoService {

	public CatalogoEstatus findCatalogoById(Integer idEstatus);
}
