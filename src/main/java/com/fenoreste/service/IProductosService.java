package com.fenoreste.service;

import org.springframework.stereotype.Service;

import com.fenoreste.entity.Productos;

@Service
public interface IProductosService {

	public Productos findProductoById(Integer idproducto);
	
}
