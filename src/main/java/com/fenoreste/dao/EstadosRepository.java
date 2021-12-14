package com.fenoreste.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fenoreste.entity.Estados;

public interface EstadosRepository extends CrudRepository<Estados, Long> {

	@Query(value="SELECT * FROM estados WHERE idestado =?1",nativeQuery=true)
	Estados findEstadoById(Integer id);
}
