package com.fenoreste.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fenoreste.entity.Municipios;

public interface MunicipiosRepository extends CrudRepository<Municipios, Long> {

	@Query(value="SELECT * FROM municipios WHERE idmunicipio=?1",nativeQuery = true)
	Municipios findMunicipioById(Integer id);
}
