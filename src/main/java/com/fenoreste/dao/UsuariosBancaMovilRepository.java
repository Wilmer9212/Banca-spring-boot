package com.fenoreste.dao;

import javax.transaction.Transactional;

import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import com.fenoreste.entity.UsuariosBancaMovil;

public interface UsuariosBancaMovilRepository extends JpaRepository<UsuariosBancaMovil, Long> {	
	
	@Query(value = " SELECT * FROM banca_movil_usuarios_bankingly WHERE " + "idorigen= ?1 " + " AND   idgrupo = ?2"
			+ " AND   idsocio = ?3", nativeQuery = true)
	public UsuariosBancaMovil findByOgs(Integer idorigen, Integer idgrupo, Integer idsocio);

	@Transactional
	@Modifying
	@Query(value = " INSERT INTO banca_movil_usuarios_bankingly VALUES (?1,?2,?3,?4,?5,?6,?7,?8)", nativeQuery = true)
	void save(Integer idorigen, Integer idgrupo, Integer idsocio,String alias,Integer idorigenp,Integer idproducto,Integer idauxiliar,boolean estatus);
     
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE banca_movil_usuarios_bankingly SET alias_usuario =?1 WHERE idorigen=?2 AND idgrupo=?3 AND idsocio=?4" , nativeQuery=true)
	void modificar(String username,Integer idorigen,Integer idgrupo,Integer idsocio);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM banca_movil_usuarios_bankingly WHERE idorigen=?1 AND idgrupo=?2 AND idsocio=?3" , nativeQuery=true)
	void eliminar(Integer idorigen,Integer idgrupo,Integer idsocio);
	
}
