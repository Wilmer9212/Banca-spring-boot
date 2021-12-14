package com.fenoreste.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fenoreste.entity.TiposCuentaBankingly;



public interface TiposCuentaBankinglyRepository extends CrudRepository<TiposCuentaBankingly, Long> {

	@Query(value = "SELECT * FROM tipos_cuenta_bankingly WHERE idproducto=?1" , nativeQuery = true)
	TiposCuentaBankingly findTipoCuentaById(Integer idproducto);
}
