package com.fenoreste.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.fenoreste.entity.Persona;

public interface GeneralRepository extends CrudRepository<Persona,Long> {

	@Query(value = "SELECT nombre FROM personas limit 1", nativeQuery = true)
	String activo();
}
