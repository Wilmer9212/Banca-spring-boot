package com.fenoreste.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fenoreste.entity.CatalogoEstatus;

public interface CatalogoRepository extends CrudRepository<CatalogoEstatus, Long>{
	
	@Query(value="SELECT * FROM catalogo_status_bankingly WHERE statusdb=?1", nativeQuery = true)
	CatalogoEstatus findCatalogoByEstatus(Integer idestatus);	

}
