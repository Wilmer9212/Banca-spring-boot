package com.fenoreste.dao;

import java.util.Date;

import javax.transaction.Transactional;

import org.hibernate.annotations.Cache;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fenoreste.entity.RegistraMovimiento;

public interface ProcesaMovimientoRepository extends JpaRepository<RegistraMovimiento , Long> {

	@Transactional
	@Modifying
	@CacheEvict
	@Query(value = " INSERT INTO bankingly_movimientos_ca"
			+ "(fecha,idusuario,sesion,referencia,idorigen,idgrupo,idsocio,idorigenp,idproducto,idauxiliar,cargoabono,monto,iva,tipo_amort,sai_aux) "
			+ "VALUES (?1,?2,?3,?4,?5,?6,?7,?8,?9,?10,?11,?12,?13,?14,?15)", nativeQuery = true)
	void saves(Date fecha,
			  Integer idusuario,
			  String sesion,
			  String referencia,
			  Integer idorigen,
			  Integer idgrupo,
			  Integer idsocio,			  
			  Integer idorigenp,
			  Integer idproducto,
			  Integer idauxiliar,			  
			  Integer cargoabono,
			  Double monto,
			  Double iva,
			  Integer tipo_amortizacion,
			  String sai);
	
	
	
}
