package com.fenoreste.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fenoreste.entity.FoliosTarjeta;

public interface FoliosTarjetasRepository extends CrudRepository<FoliosTarjeta,Long> {

	@Query(value = "SELECT w.* FROM ws_siscoop_folios_tarjetas w  INNER JOIN ws_siscoop_tarjetas td using(idtarjeta)"
			+ "  WHERE w.idorigenp = ?1"
			+ "  AND w.idproducto = ?2"
			+ "  AND w.idauxiliar = ?3"
			+ "  AND td.fecha_vencimiento > (select distinct fechatrabajo from origenes limit 1)", nativeQuery = true)
	FoliosTarjeta findFolioByOpa(int idorigenp,int idproducto,int idauxiliar);
	


}
