package com.fenoreste.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fenoreste.entity.Tablas;

public interface TablasRepository extends JpaRepository<Tablas, Long> {
	
	// Buscar tabla
	@Query(value = "SELECT * FROM tablas WHERE  idtabla=?1 AND idelemento=?2", nativeQuery = true)
	Tablas find1(String idtabla, String idelemento);
	
	Tablas findTablasByIdtablaAndIdelemento(String idtabla,String idelmento);

}
