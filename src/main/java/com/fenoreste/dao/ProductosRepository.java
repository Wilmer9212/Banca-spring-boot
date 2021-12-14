package com.fenoreste.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fenoreste.entity.Productos;

public interface ProductosRepository extends CrudRepository<Productos, Long> {

	@Query(value = "SELECT * FROM productos WHERE idproducto=?1" , nativeQuery = true)
	Productos findProductoById(Integer idproducto);
}
