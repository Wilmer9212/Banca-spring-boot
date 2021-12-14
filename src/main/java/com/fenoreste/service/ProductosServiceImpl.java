package com.fenoreste.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fenoreste.dao.ProductosRepository;
import com.fenoreste.entity.Productos;

@Service
public class ProductosServiceImpl  implements IProductosService{
	
	@Autowired
	ProductosRepository productosDao;
	
	@Override
	public Productos findProductoById(Integer idproducto) {
		// TODO Auto-generated method stub
		return productosDao.findProductoById(idproducto);
	}

}
