package com.fenoreste.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fenoreste.entity.UsuariosBancaMovil;

public interface OtrosRepository extends CrudRepository<UsuariosBancaMovil, Long> {

	@Query(value = " SELECT count(*) FROM sopar WHERE " + "       idorigen= ?1 " + " AND   idgrupo = ?2"
			+ " AND   idsocio = ?3" + " AND   tipo = ?4", nativeQuery = true)
	Integer count_sopar(Integer idorigen, Integer idgrupo, Integer idsocio, String tipo);
	
	@Query(value = "SELECT text(pg_backend_pid())||'-'||trim(to_char(now(),'ddmmyy'))" , nativeQuery = true)
	String sesion();
	
	@Query(value ="SELECT sai_bankingly_servicio_activo_inactivo()" , nativeQuery = true)
	boolean servicios_activos();
	
}
