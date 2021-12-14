package com.fenoreste.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fenoreste.entity.Auxiliares;
import com.fenoreste.entity.Persona;

public interface AuxiliaresRepository extends CrudRepository<Auxiliares, Long> {

	@Query(value = "SELECT * FROM auxiliares WHERE idorigenp=?1  AND idproducto=?2 AND idauxiliar=?3", nativeQuery = true)
	Auxiliares findAuxiliarByOPA(Integer idorigenp, Integer idproducto, Integer idauxiliar);

	// Producto para banca movil
	@Query(value = "SELECT * FROM auxiliares WHERE idorigen=?1  AND idgrupo=?2 AND idsocio=?3 AND idproducto=?4 AND estatus=0", nativeQuery = true)
	Auxiliares findAuxiliarBancaMovil(Integer idorigen, Integer idgrupo, Integer idsocio, Integer idproducto);

	// Auxiliares activos para socio
	@Query(value = "SELECT * FROM auxiliares WHERE idorigen=?1  AND idgrupo=?2 AND idsocio=?3 AND estatus=2", nativeQuery = true)
	List<Auxiliares> AuxiliaresActivos(Integer idorigen, Integer idgrupo, Integer idsocio);

	// Auxiliares activos para socio y un producto en especifico
	@Query(value = "SELECT * FROM auxiliares WHERE idorigen=?1  AND idgrupo=?2 AND idsocio=?3 AND idorigenp=?4 AND idproducto=?5 AND idauxiliar=?6 AND estatus=2", nativeQuery = true)
	Auxiliares AuxiliarByOpaOgs(Integer idorigen, Integer idgrupo, Integer idsocio,Integer idorigenp,Integer idproducto,Integer idauxiliar);

	// Lista de productos para banca movil activos
	@Query(value = "SELECT * FROM auxiliares a INNER JOIN tipos_cuenta_bankingly tp USING(idproducto) WHERE idorigen=?1  AND idgrupo=?2 AND idsocio=?3 and tp.producttypeid=?4 AND estatus=2", nativeQuery = true)
	List<Auxiliares> findListaCuentasBankingly(Integer idorigen, Integer idgrupo, Integer idsocio, Integer productType);

	// Lista de productos para banca movil activos
	@Modifying
	@Transactional
	@Query(value = "SELECT * FROM auxiliares a INNER JOIN tipos_cuenta_bankingly tp USING(idproducto) WHERE idorigen=?1  AND idgrupo=?2 AND idsocio=?3 AND estatus=2", nativeQuery = true)
	List<Auxiliares> findListaCuentasBankinglySinType(Integer idorigen, Integer idgrupo, Integer idsocio);

	// Solo para CSN
	@Query(value = "SELECT * FROM auxiliares WHERE idorigen=?1  AND idgrupo=?2 AND idsocio=?3 AND idproducto=?4", nativeQuery = true)
	Auxiliares findAuxiliarTdd(int idorigen, int idgrupo, int idsocio, int idproducto);
	
	// Solo para CSN
	@Query(value = "SELECT * FROM auxiliares a inner join tipos_cuenta_bankingly t using(idproducto) WHERE a.estatus=2 and idorigenp=?1  AND idproducto=?2 AND idauxiliar=?3", nativeQuery = true)
	Auxiliares findAuxiliarByOpa(int idorigenp, int idproducto, int idauxiliar);

}
