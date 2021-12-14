package com.fenoreste.service;

import org.springframework.stereotype.Service;

import com.fenoreste.entity.Estados;
import com.fenoreste.entity.Municipios;

@Service
public interface IEstadosService {

	public Estados findEstadoById(Integer id);
}
