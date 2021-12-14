package com.fenoreste.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fenoreste.entity.Paises;

public interface PaisesRepository  extends CrudRepository<Paises, Long>{

	@Query(value = "SELECT * FROM paises WHERE idpais=?1",nativeQuery = true)
	Paises findPaisById(Integer id);
}
