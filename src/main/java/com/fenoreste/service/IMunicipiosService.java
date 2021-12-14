package com.fenoreste.service;

import org.springframework.stereotype.Service;

import com.fenoreste.entity.Municipios;

@Service
public interface IMunicipiosService {

	public Municipios findMunicipioById(Integer id);
}
