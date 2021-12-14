package com.fenoreste.dao;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.CrudRepository;

import com.fenoreste.entity.Origenes;
public interface OrigenesRepository extends CrudRepository<Origenes,Long> {

	@Query(value = "SELECT * FROM origenes WHERE matriz=0 limit 1" , nativeQuery = true)
	Origenes findMatrizOrigen();
	
	@Query(value = "SELECT * FROM origenes WHERE idorigen=?1 limit 1" , nativeQuery = true)
	Origenes findOrigenById(Integer idorigen);
	
	@Query(value = "SELECT to_char(date(fechatrabajo),'yyyy-mm-dd') FROM origenes limit 1" , nativeQuery = true)
	String fechaTrabajo();

	
}
