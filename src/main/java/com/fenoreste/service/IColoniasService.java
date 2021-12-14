package com.fenoreste.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fenoreste.entity.Colonia;

@Service
public interface IColoniasService {

	public Colonia findColoniaById(Integer id);
	
}
