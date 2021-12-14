package com.fenoreste.service;

import org.springframework.stereotype.Service;

import com.fenoreste.entity.ProductoTercero;

@Service
public interface ITercerosService {

	public ProductoTercero findTerceroByCuenta(String cuentaTercero); 
	
	public void guardar(ProductoTercero terero);
}
