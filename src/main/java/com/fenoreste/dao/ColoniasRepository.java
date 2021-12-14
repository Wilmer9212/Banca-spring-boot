package com.fenoreste.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fenoreste.entity.Colonia;

public interface ColoniasRepository  extends CrudRepository<Colonia,Integer>{

	@Query(value = "SELECT * FROM colonias WHERE idcolonia = ?1", nativeQuery = true)
	Colonia findColoniaById(Integer id);
}
