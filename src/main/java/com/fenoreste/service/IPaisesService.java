package com.fenoreste.service;

import org.springframework.stereotype.Service;

import com.fenoreste.entity.Paises;

@Service
public interface IPaisesService {

	public Paises findPaisById(Integer id);
}
