package com.fenoreste.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fenoreste.entity.ProductoTercero;

public interface TercerosRepository extends JpaRepository<ProductoTercero, Long> {

	@Query(value = "SELECT * FROM productos_terceros_bankingly WHERE thirdpartyproductbankidentifier=?1", nativeQuery = true)
	ProductoTercero findTerceroByCuenta(String cuentaTercero);
	
	
	
}
